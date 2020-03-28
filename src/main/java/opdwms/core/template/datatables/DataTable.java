package opdwms.core.template.datatables;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

/**
 * Datatables component
 * <p>
 * This class responsible for allowing datatables to fetch information from the
 * web server as opposed to fetching all the information at once and then
 * showing the table using datatables.
 * <p>
 * This class uses the HQL select syntax identified as follows: select_statement
 * :: = [select_clause] from_clause [where_clause] [groupby_clause]
 * [having_clause] [orderby_clause]
 *
 * @author Ignatius
 * @version 1.0.0
 * @category Datatables
 * @package Dev
 * @since Nov 05, 2018
 */
@Transactional(readOnly = true)
@Service("dataTableService")
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DataTable implements DatatablesInterface {

    private final List<RowFormatInterface> _formatChain = new ArrayList<>();
    private final Map<String, Collection> _listParams = new HashMap<>();
    private final Map<String, Object> _params = new HashMap<>();
    private final Map<String, Object> _footerColumns = new LinkedHashMap<>();
    private final Map<String, Object> _orderingColumns = new HashMap<>();
    private final List<String> _whereParams = new ArrayList<>();
    private final List<String> _columnNames = new ArrayList<>();
    private final StringBuilder _selectParams = new StringBuilder();
    private final StringBuilder _fromParams = new StringBuilder();
    private boolean _nativeSQL = false;
    private String _groupBy = "";
    private String _esDocument;
    private String[] _esDocFields;
    @Autowired
    private HttpServletRequest _request;
    @Autowired
    private RestHighLevelClient client;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public DataTable esDocument(String docName) {
        this._esDocument = docName;
        return this;
    }

    @Override
    public DataTable esFields(String... fields) {

        this._esDocFields = fields.clone();
        return this;
    }


    @Override
    public Map<String, Object> showEsTable() {
        Map<String, Object> resp = new HashMap<>();
        String s = _request.getParameter("sSearch");
        SearchRequest request = new SearchRequest(_esDocument);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        if (ObjectUtils.isEmpty(s)) {
            sourceBuilder.query(QueryBuilders.matchAllQuery());
        } else {
            BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
            for (String field : _esDocFields) {
                boolBuilder = boolBuilder.should(QueryBuilders.wildcardQuery(field, "*".concat(s).concat("*")));
            }
            sourceBuilder.query(boolBuilder);
        }
        int sortFieldIndex = Integer.parseInt(_request.getParameter("iSortCol_0"));
        int limit = Integer.parseInt(_request.getParameter("iDisplayLength"));
        int page = Integer.parseInt(_request.getParameter("iDisplayStart"));
        String sortDir = _request.getParameter("sSortDir_0");

        sourceBuilder.from(page)
                .size(limit)
                .sort(new FieldSortBuilder(_esDocFields[sortFieldIndex]).order(SortOrder.fromString(sortDir)));

        request.source(sourceBuilder);
        List<Object[]> data = new ArrayList<>();
        int sEcho = Integer.parseInt(_request.getParameter("sEcho"));

        resp.put("sEcho", sEcho);
        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            System.out.println("totalHits: " + response.getHits().getTotalHits());
            SearchHit[] searchHits = response.getHits().getHits();
            for (SearchHit hit : searchHits) {
                // do something with the SearchHit
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                Object[] docValues = new Object[_esDocFields.length];
                for (int x = 0; x < _esDocFields.length; x++) {
                    docValues[x] = sourceAsMap.get(_esDocFields[x]);
                }
                data.add(docValues);
            }

            resp.put("aaData", data);
            resp.put("iTotalRecords", response.getHits().getTotalHits());
            resp.put("iTotalDisplayRecords", response.getHits().getTotalHits());

        } catch (IOException e) {
            e.printStackTrace();

            resp.put("iTotalRecords", 0);
            resp.put("iTotalDisplayRecords", 0);
            resp.put("aaData", data);
        }
        return resp;
    }

    /**
     * When one just wants to return an empty result set
     *
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> emptyResultSet() {
        Map<String, Object> map = new HashMap<>();

        int sEcho = Integer.parseInt(_request.getParameter("sEcho"));

        map.put("sEcho", sEcho);
        map.put("iTotalRecords", 0);
        map.put("iTotalDisplayRecords", 0);

        Map<String, Object> data = new HashMap<>();
        map.put("aaData", data);

        return map;
    }

    /**
     * Indicate whether we have prepared a native sql statement
     *
     * @param state
     * @return DatatablesInterface
     */
    @Override
    public DataTable nativeSQL(boolean state) {
        _nativeSQL = true;
        return this;
    }

    /**
     * Specify the columns that will appear in the final result set in order to
     * assist this class in building the information needed to render the
     * datatable result map
     *
     * @param select
     * @return DatatablesInterface
     */
    @Override
    public DataTable select(String select) {
        select = select.trim().replaceAll(" +", " ");
        int c, result = 0;

        for (String col : select.split(",")) {
            // Remove the extra spaces
            col = col.trim().replace(";", ",");

            // Set the column used
            c = col.toLowerCase().indexOf(" as ");
            if (c > 0) col = col.substring(0, c);
            _columnNames.add(col);

            // Append it to the select statement builder
            if (_selectParams.length() > 0)
                _selectParams.append(", ").append(col);
            else _selectParams.append(col);

            // Append the column identifier
            _selectParams.append(" AS dtcol_").append(result);

            // Set the column to use for counting the result set
            result++;
        }

        // Allow the chaining of the params
        return this;
    }

    /**
     * Specify the tables where the information will be fetched from i.e. the
     * parent table and all the respective joins
     *
     * @param from
     * @return DatatablesInterface
     */
    @Override
    public DataTable from(String from) {
        // Set the table stuff
        if (_fromParams.length() > 0)
            _fromParams.append(" ").append(from);
        else _fromParams.append(from);

        // Allow the chaining of the params
        return this;
    }

    /**
     * Specify the conditions that will be applied to the query. This will help
     * in building the filter used by datatables
     *
     * @param where
     * @return DatatablesInterface
     */
    @Override
    public DataTable where(String where) {
        _whereParams.add(where);

        // Allow the chaining of the params
        return this;
    }

    /**
     * Apply the group by clause in order to properly support the aggregate
     * functions
     *
     * @param groupBy
     * @return DatatablesInterface
     */
    @Override
    public DataTable groupBy(String groupBy) {
        _groupBy = groupBy;

        // Allow chaining
        return this;
    }

    /**
     * Set the formatter that will be used to format the response generated by
     * the class
     *
     * @param formatter
     * @return DatatablesInterface
     */
    @Override
    public DataTable setFormatter(RowFormatInterface formatter) {
        // Check if the filter is in the chain
        if (!_formatChain.contains(formatter))
            _formatChain.add(formatter);

        // Set the
        return this;
    }

    /**
     * Set the parameter bound to the parameterised query passed in the
     * conditions
     *
     * @param key
     * @param value
     * @return DatatablesInterface
     */
    @Override
    public DataTable setParameter(String key, Object value) {
        // Check if the key has been set`11
        _params.put(key, value);

        // Allow the chaining of the params
        return this;
    }

    /**
     * Bind multiple values to a named query parameter.
     *
     * @param key
     * @param value
     * @return DatatablesInterface
     */
    @Override
    public DataTable setParameterList(String key, Collection value) {
        // Place the collection in the parameter bag
        _listParams.put(key, value);

        // Allow chaining
        return this;
    }

    /**
     * Allow one to specify the columns for footer totals
     *
     * @param key
     * @param columnName
     * @return DataTable
     */
    @Override
    public DataTable setFooterColumn(String key, String columnName) {
        //Place the collection in the footer bag
        _footerColumns.put(key, columnName);
        //Allow the chaining of the params
        return this;
    }

    /**
     * Allow one to specify the columns to order
     *
     * @param columns
     * @param order
     * @return
     */
    @Override
    public DataTable setOrderingColumns(String columns, String order) {
        _orderingColumns.put(columns, order);
        //Allow the chaining of the params
        return this;
    }

    /**
     * Get the HQL that will be used to generate the result set
     *
     * @param setting
     * @return String
     */
    @Override
    public String getHQL(String setting) {
        return buildHQL((null == setting) ? "" : setting).toString();
    }

    /**
     * Get the parameters used to generate the result set
     *
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> map = new HashMap<>();

        map.put("params", _params);
        map.put("listParams", _listParams);

        return map;
    }

    /**
     * Allow one to add multiple parameters
     *
     * @param map
     * @return DatatablesInterface
     */
    @Override
    public DataTable setParameters(Map<String, Object> map) {
        for (Map.Entry<String, Object> p : map.entrySet())
            _params.put(p.getKey(), p.getValue());

        // Allow the chaining of the params
        return this;
    }

    /**
     * The following function generates the map used to render the result set
     *
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> showTable() {
        Map<String, Object> map = new HashMap<>();

        // The result set
        map.put("sEcho", Integer.parseInt(_request.getParameter("sEcho")));
//        map.put("iTotalRecords", buildResultSet("total").iterator().next());
//        map.put("iTotalDisplayRecords", buildResultSet("filtered-total").iterator().next());

        List<Object[]> total = buildResultSet("total");
        map.put("iTotalRecords", total.iterator().next());
        map.put("iTotalDisplayRecords",  total.iterator().next());
        if (!_footerColumns.isEmpty()) {
            map.put("footerTotals",  total.iterator().next());
//            map.put("footerTotals", buildResultSet("footer-totals"));
        }

        // If the formatter has not been set
        if (_formatChain.isEmpty()) {
            map.put("aaData", buildResultSet(""));
        }

        // When formatting the row
        else {
            List<Object> aaData = new ArrayList<>();

            for (Object[] row : buildResultSet("")) {
                // Loop through the chain
                for (RowFormatInterface fmt : _formatChain)
                    row = fmt.formatRow(row);

                // Set the formated data
                aaData.add(row);
            }

            map.put("aaData", aaData);
        }

        // Return the result set
        return map;
    }

    @Override
    public Map<String, Object> showTable(Function<Object[], Object[]> func) {
        Map<String, Object> map = new HashMap<>();

        // The result set
        map.put("sEcho", Integer.parseInt(_request.getParameter("sEcho")));
        map.put("iTotalRecords", buildResultSet("total").iterator().next());
        map.put("iTotalDisplayRecords", buildResultSet("filtered-total").iterator().next());

        if (!_footerColumns.isEmpty()) {
            map.put("footerTotals", buildResultSet("footer-totals"));
        }

        List<Object> aaData = new ArrayList<>();

        // If the formatter has not been set
        if (_formatChain.isEmpty()) {
            for (Object[] row : buildResultSet("")) {
                // Set the formated data
                aaData.add(func.apply(row));
            }
        }
        // When formatting the row
        else {
            for (Object[] row : buildResultSet("")) {
                Object[] row2 = new Object[6];

                row2 = func.apply(row);
                // Loop through the chain
                for (RowFormatInterface fmt : _formatChain)
                    row2 = fmt.formatRow(row2);

                // Set the formated data
                aaData.add(row2);
            }
        }

        map.put("aaData", aaData);

        // Return the result set
        return map;
    }

    /**
     * Build the query result set
     *
     * @param setting
     * @return List<Object[]>
     */
    private List<Object[]> buildResultSet(String setting) {
        // Create the session
        Session session = entityManager.unwrap(Session.class);
        StringBuilder hql = buildHQL(setting);

        // Specify the limit applied to the result set
        Query q = _nativeSQL ? session.createNativeQuery(hql.toString()) : session.createQuery(hql.toString());

        if (!setting.equals("filtered-total") && !setting.equals("total") && !setting.equals("footer-totals")) {
            q = setLimit(hql, session);
        }

        // Set the parameters needed
        if (!_params.isEmpty()) {
            for (Map.Entry<String, Object> p : _params.entrySet()) {
                try {
                    q.setParameter(p.getKey(), p.getValue());

//                    if (!setting.equals("filtered-total") && !setting.equals("total"))
//                        System.out.println(p.getKey() + ": " + p.getValue().toString());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        if (!_listParams.isEmpty()) {
            for (Map.Entry<String, Collection> p : _listParams.entrySet()) {
                try {
                    q.setParameterList(p.getKey(), p.getValue());

//                    if (!setting.equals("filtered-total") && !setting.equals("total"))
//                        System.out.println(p.getKey() + ": " + p.getValue().toString());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        // Get the result set
        List<Object[]> list = q.list();

        // Return the results
        return list;
    }


    /**
     * Build the HQL used when building the query
     *
     * @param setting
     * @return String
     */
    private StringBuilder buildHQL(String setting) {
        // The HQL to build
        StringBuilder hql = new StringBuilder();
        StringBuilder ss = new StringBuilder();

        // If we are getting the number of records
        if (setting.equals("filtered-total") || setting.equals("total")) {
            if (!_nativeSQL) {
                String col = "";

                for (int i = 0; i < _columnNames.size(); i++) {
                    if (!_columnNames.get(i).contains("(")) {
                        col = _columnNames.get(i);
                        break;
                    }
                }

                if (!col.isEmpty()) {
                    col = (_groupBy.contains(col)) ? "DISTINCT " + col : col;
                    hql.append("SELECT COUNT(").append(col).append(") FROM ").append(_fromParams);
                }
            } else {
                hql.append("SELECT ").append(_selectParams).append(" FROM ").append(_fromParams);
            }
        }

        //When getting footer column totals
        else if (setting.equals("footer-totals")) {
            if (!_footerColumns.isEmpty()) {
                StringBuilder columns = new StringBuilder();
                for (Map.Entry<String, Object> p : _footerColumns.entrySet()) {
                    try {
                        if (columns.length() > 0) columns.append(", ");
                        columns.append(p.getValue()).append("(").append(p.getKey()).append(") ");

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                hql.append("SELECT ").append(columns).append("FROM ").append(_fromParams);
            }
        }

        // when generating the statement
        else hql.append("SELECT ").append(_selectParams).append(" FROM ").append(_fromParams);

        //Add the conditions
        if (_whereParams.size() > 0) {
            for (String item : _whereParams) {
                if (ss.length() > 0) ss.append(" AND ");
                ss.append("(").append(item).append(")");
            }

            hql.append(" WHERE ").append(ss);
        }

        String groupByStatement = "";
        if (!_groupBy.isEmpty() && !setting.equals("footer-totals"))
            groupByStatement = String.format(" GROUP BY %s ", _groupBy);

        //The search params
        String filterParams = setFilter(setting).toString();

        //When using HAVING clause
        if (hasAggregateFunctions(filterParams)) {

            //Append the GROUP BY clause
            hql.append(groupByStatement);

            //Append the filter params
            if (!StringUtils.isEmpty(filterParams))
                hql.append(" HAVING ").append(filterParams);
        }

        //When using the WHERE clause
        else {
            //Append the filter params when present
            if (!StringUtils.isEmpty(filterParams)) {
                hql.append((_whereParams.size() > 0) ? " AND " : " WHERE ").append(filterParams);
            }

            //When to append GROUP BY clause
            if (!StringUtils.isEmpty(groupByStatement))
                hql.append(groupByStatement);

        }

        // Add the order that will be applied to the result set
        if (!setting.equals("filtered-total") && !setting.equals("total") && !setting.equals("footer-totals")) {
            ss = setOrder();
            if (null != ss && ss.length() > 0)
                hql.append(" ORDER BY ").append(ss);
        }

        // If we are implementing a native sql
        if (_nativeSQL && (setting.equals("filtered-total") || setting.equals("total"))) {
            ss = new StringBuilder();
            ss.append("SELECT COUNT(cc.dtcol_0) FROM (").append(hql).append(") cc");
            hql = ss;
        }

        // The string built
        return hql;
    }


    /**
     * Create the filter query used to filter the information in the result set
     *
     * @return String
     */
    private StringBuilder setFilter(String setting) {
        StringBuilder sFilter = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        String s, t;

        if (!setting.equals("total")) {
            s = _request.getParameter("sSearch");
            if (!s.isEmpty()) {
                for (int i = 0; i < Integer.parseInt(_request.getParameter("iColumns")); i++) {
                    if (_request.getParameter("bSearchable_" + i).equals("true")) {
                        if (sb.length() > 0) sb.append(" OR ");
                        sb.append("lower(").append(_columnNames.get(i)).append(")").append(" LIKE lower(:dtSearch)");
                    }
                }

                _params.put("dtSearch", "%" + s + "%");
                sFilter.append("(").append(sb).append(")");
            }

            sb.setLength(0);
            for (int i = 0; i < Integer.parseInt(_request.getParameter("iColumns")); i++) {
                s = _request.getParameter("sSearch_" + i);
                if (_request.getParameter("bSearchable_" + i).equals("true") && !s.isEmpty()) {

                    if (sb.length() > 0) sb.append(" AND ");
                    sb.append("lower(").append(_columnNames.get(i)).append(")").append(" LIKE lower(:__sSearch_)").append(i);
                    _params.put("__sSearch_" + i, "%" + s + "%");

                }
            }

            if (sb.length() > 0) sFilter.append("(").append(sb).append(")");
        }

        // Get the filter
        return sFilter;
    }

    /**
     * Omit the aggregate functions as you set the filter
     *
     * @param column
     * @return Boolean
     */
    @Deprecated
    private boolean checkColumn(String column) {
        String temp = column.toUpperCase().replace(" ", "");
        return !(temp.startsWith("MIN") || temp.startsWith("MAX(") || temp.startsWith("SUM(") || temp.startsWith("COUNT("));
    }

    private boolean hasAggregateFunctions(String query) {
        return (
                query.contains("MIN") ||
                        query.contains("MAX") ||
                        query.contains("SUM") ||
                        query.contains("COUNT")
        );
    }

    /**
     * Set the aux conditions specified in the request parameters used to limit
     * and order the result set
     *
     * @return String
     */
    private StringBuilder setOrder() {
        // If there are no parameters defined
        if (_request.getParameter("iSortCol_0").isEmpty())
            return null;

        // Build the order
        StringBuilder sOrder = new StringBuilder();

        if (!_orderingColumns.isEmpty()) {
            for (Map.Entry<String, Object> p : _params.entrySet()) {
                sOrder.append(p.getKey()).append(" ").append(p.getValue());
                break;
            }
        } else {
            int iSortingCols = Integer.parseInt(_request.getParameter("iSortingCols"));
            int iSortCol;

            for (int i = 0; i < iSortingCols; i++) {
                iSortCol = Integer.parseInt(_request.getParameter("iSortCol_" + i));
                if (_request.getParameter("bSortable_" + i).equals("true")) {
                    if (sOrder.length() > 0) sOrder.append(", ");
                    sOrder.append(_columnNames.get(iSortCol)).append(" ");
                    sOrder.append((_request.getParameter("sSortDir_" + i).equals("asc")) ? "ASC" : "DESC");
                }
            }
        }

        // The order query
        return sOrder;
    }

    /**
     * Define the limit that will be applied to the result set
     *
     * @param hql
     * @param session
     * @return Query
     */
    private Query setLimit(StringBuilder hql, Session session) {
        Query q = _nativeSQL ? session.createNativeQuery(hql.toString()) : session.createQuery(hql.toString());
        int iDisplayStart = Integer.parseInt(_request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(_request.getParameter("iDisplayLength"));

        if (!_request.getParameter("iDisplayStart").isEmpty() && iDisplayStart != -1) {
            int offset = iDisplayStart / iDisplayLength;
            q.setFirstResult(offset * iDisplayLength);
            q.setMaxResults(iDisplayLength);
        }

        return q;
    }


}
