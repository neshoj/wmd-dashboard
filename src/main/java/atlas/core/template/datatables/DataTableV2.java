package atlas.core.template.datatables;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @category    Datatables
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Service
@Transactional(readOnly = true)
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DataTableV2 implements DataTablesInterfaceV2{


    private final List<String> _whereParams = new ArrayList<>();
    private final List<String> _columnNames = new ArrayList<>();
    private final StringBuilder _selectParams = new StringBuilder();
    private final StringBuilder _fromParams = new StringBuilder();

    private final Map<String, Collection> _listParams = new HashMap<>();
    private final Map<String, Object> _params = new HashMap<>();
    private String _groupBy = "";

    private boolean _nativeSQL = false;

    @Autowired
    private HttpServletRequest _request;
    @PersistenceContext private EntityManager entityManager;


    /**
     * When one just wants to return an empty result set
     *
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> emptyResultSet(){
        Map<String, Object> map = new HashMap<>();

        int draw = Integer.parseInt( _request.getParameter("draw") );

        map.put("draw", draw);
        map.put("recordsTotal", 0);
        map.put("recordsFiltered", 0);

        Map<String, Object> data = new HashMap<>();
        map.put("data", data);

        return map;

    }

    /**
     * Indicate whether we have prepared a native sql statement
     *
     * @param state
     * @return DataTable
     */
    @Override
    public DataTableV2 nativeSQL(boolean state) {
        _nativeSQL = true;
        return this;
    }

    /**
     * Specify the columns that will appear in the final result set in order to
     * assist this class in building the information needed to render the
     * datatable result map
     *
     * @param select
     * @return DataTable
     */
    @Override
    public DataTableV2 select(String select) {
        select = select.trim().replaceAll(" +", " ");
        int c, result = 0;

        for (String col : select.split(",")) {
            // Remove the extra spaces
            col = col.trim().replace(";", ",");

            // Set the columns used
            c = col.toLowerCase().indexOf(" as ");
            if (c == -1) {
                _columnNames.add(col);
            } else {
                _columnNames.add(col.substring(0, c));
            }

            // Append it to the select statement builder
            if (_selectParams.length() > 0) {
                _selectParams.append(", ").append(col);
            } else {
                _selectParams.append(col);
            }

            // Append the column identifier
//            _selectParams.append(" AS dtcol_").append(result);
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
     * @return DataTable
     */
    @Override
    public DataTableV2 from(String from) {
        // Set the table stuff
        if (_fromParams.length() > 0) {
            _fromParams.append(" ").append(from);
        } else {
            _fromParams.append( from );
        }

        // Allow the chaining of the params
        return this;
    }

    /**
     * Specify the conditions that will be applied to the query. This will help
     * in building the filter used by datatables
     *
     * @param where
     * @return DataTable
     */
    @Override
    public DataTableV2 where(String where) {
        _whereParams.add(where);

        // Allow the chaining of the params
        return this;
    }

    /**
     * Apply the group by clause in order to properly support the aggregate
     * functions
     *
     * @param groupBy
     * @return DataTable
     */
    @Override
    public DataTableV2 groupBy(String groupBy) {
        _groupBy = groupBy;

        // Allow chaining
        return this;
    }

    @Override
    public DataTableV2 setFormatter(RowFormatInterface formatter) {
        return null;
    }

    /**
     * Set the parameter bound to the parameterised query passed in the
     * conditions
     *
     * @param key
     * @param value
     * @return DataTable
     */
    @Override
    public DataTableV2 setParameter(String key, Object value) {
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
     * @return DataTable
     */
    @Override
    public DataTableV2 setParameterList(String key, Collection value) {
        // Place the collection in the parameter bag
        _listParams.put(key, value);

        // Allow chaining
        return this;
    }

    /**
     * Allow one to add multiple parameters
     *
     * @param map
     * @return DataTable
     */
    @Override
    public DataTableV2 setParameters(Map<String, Object> map) {
        for (Map.Entry<String, Object> p : map.entrySet()) {
            _params.put(p.getKey(), p.getValue());
        }

        // Allow the chaining of the params
        return this;
    }

    @Override
    public String getHQL(String setting) {
        return null;
    }

    @Override
    public Map<String, Object> getParameters() {
        return null;
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
        System.err.println("draw:" +  Integer.parseInt(_request.getParameter("draw")));
        map.put("draw", Integer.parseInt(_request.getParameter("draw")));
        map.put("recordsTotal", buildResultSet("total").iterator().next());
        map.put("recordsFiltered", buildResultSet("filtered-total").iterator().next());
        map.put("data", buildResultSet("") );

        // Return the result set
        return map;
    }

    @Override
    public Map<String, Object> showTable(Function<Object[], Object[]> func) {
        return null;
    }

    @Override
    public DataTableV2 setFooterColumn(String key, String columnName) {
        return null;
    }

    @Override
    public DataTableV2 setOrderingColumns(String columns, String order) {
        return null;
    }

    /**
     * Build the query result set
     *
     * @param setting
     * @return List<Object[]>
     */
    private List<Object[]> buildResultSet(String setting) {
        // Create the session
        Session session = entityManager.unwrap( Session.class );
        StringBuilder hql = buildHQL( setting );

        // Specify the limit applied to the result set
        Query q = _nativeSQL ? session.createNativeQuery(hql.toString()) : session.createQuery(hql.toString());
        if (!setting.equals("filtered-total") && !setting.equals("total")) {
            q = setLimit( hql, session );
            System.err.println( hql );
        }

        System.err.println("==============================================================");
        System.err.println( hql );
        System.err.println("==============================================================");

        // Set the parameters needed
        if (!_params.isEmpty()) {
            for (Map.Entry<String, Object> p : _params.entrySet()) {
                try {
                    q.setParameter(p.getKey(), p.getValue());

                    if ( !setting.equals("filtered-total") && !setting.equals("total") ) {
                        System.err.println( p.getKey() + ": " + p.getValue().toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e.getMessage());
                }
            }
        }

        if (!_listParams.isEmpty()) {
            for (Map.Entry<String, Collection> p : _listParams.entrySet()) {
                try {
                    q.setParameterList(p.getKey(), p.getValue());

                    if ( !setting.equals("filtered-total") && !setting.equals("total") ) {
                        System.err.println(p.getKey() + ": " + p.getValue().toString());
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
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
        if ( setting.equals("filtered-total") || setting.equals("total")) {
            if ( false == _nativeSQL ) {
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
                hql.append("SELECT ").append( _selectParams ).append(" FROM ").append( _fromParams );
            }
        } // when generating the statement
        else {
            hql.append("SELECT ").append( _selectParams ).append(" FROM ").append( _fromParams );
        }

        // Add the conditions
        if ( _whereParams.size() > 0 ) {
            for (String item : _whereParams) {
                if (ss.length() > 0) {
                    ss.append(" AND ");
                }
                ss.append("(").append(item).append(")");
            }

            hql.append(" WHERE ").append(ss);
        }

        // Add the filter
        ss = setFilter( setting );
        if (ss.length() > 0) {
            hql.append((_whereParams.size() > 0) ? " AND " : " WHERE ").append(ss);
        }

        // Set the group by clause
        if (!_groupBy.isEmpty()) {
            hql.append(" GROUP BY ").append(_groupBy);
        }

        // Add the order that will be applied to the result set
        if (!setting.equals("filtered-total") && !setting.equals("total")) {
            ss = setOrder();
            if (null != ss && ss.length() > 0) {
                hql.append(" ORDER BY ").append(ss);
            }
        }

        // If we are implementing a native sql
        if (_nativeSQL && (setting.equals("filtered-total") || setting.equals("total"))) {
            ss = new StringBuilder();
            ss.append("SELECT COUNT(*) FROM (").append( hql ).append(") t");
            hql = ss;
        }

        // The string built
        return hql;
    }

    /**
     * Define the limit that will be applied to the result set
     *
     * @param hql
     * @param session
     * @return Query
     */
    private Query setLimit(StringBuilder hql, Session session) {
        Query q = _nativeSQL ? session.createNativeQuery( hql.toString() ) : session.createQuery( hql.toString() );
        int start = Integer.parseInt( _request.getParameter("start") );
        int length = Integer.parseInt( _request.getParameter("length") );

        if (!_request.getParameter("start").isEmpty() && length != -1 ) {
            q.setFirstResult( start );
            q.setMaxResults( length );
        }

        return q;
    }

    /**
     * Create the filter query used to filter the information in the result set
     *
     * @return String
     */
    private StringBuilder setFilter(String setting) {
        System.err.println("============== SETTING FILTER ===============");
        StringBuilder sFilter = new StringBuilder();
        StringBuilder gSearch = new StringBuilder();
        StringBuilder columnSearch = new StringBuilder();
        String s;
        int columnIndex;

        Map<String, String[]> map =_request.getParameterMap();
        Map<String, Object> columnsMap = map.entrySet().stream()
                .filter( p -> p.getKey().startsWith("columns") && p.getKey().endsWith("[searchable]")  )
                .collect( Collectors.toMap(p -> p.getKey(), p -> {
                    String[] values = p.getValue();
                    String actualValue = values[0];
                    return actualValue;
                }) );


        //Set global search
        if ( !setting.equals("total") ) {
            s = _request.getParameter("search[value]");

            System.err.println( "s:" + s );

            if ( !StringUtils.isEmpty( s ) ) {
                for (Map.Entry<String, Object> entry : columnsMap.entrySet()) {
                    columnIndex = Integer.parseInt( entry.getKey().replaceAll("[^\\d]", "") );
                    if (checkColumn(_columnNames.get( columnIndex ))) {
                        if (gSearch.length() > 0) {
                            gSearch.append(" OR ");
                        }

                        gSearch
                                .append( "lower(" )
                                .append( _columnNames.get( columnIndex ) )
                                .append( ")" )
                                .append( " LIKE lower(:gSearch)" );
                    }
                }

                _params.put("gSearch", "%" + s + "%");
//                sFilter.append( "(" ).append( sb ).append( ")" );
            }

            //Individual column search
//            sb.setLength(0);
            for (Map.Entry<String, Object> entry : columnsMap.entrySet()) {
                columnIndex = Integer.parseInt( entry.getKey().replaceAll("[^\\d]", "") );
                if( entry.getValue().equals("true") && !s.isEmpty() ){

                    if ( checkColumn(_columnNames.get( columnIndex )) ) {
                        if (columnSearch.length() > 0) {
                            columnSearch.append(" AND ");
                        }

                        columnSearch
                                .append("lower(")
                                .append( _columnNames.get( columnIndex ) )
                                .append(") ")
                                .append(" LIKE lower(:_search)")
                                .append( _request.getParameter(String.format("columns[%d][search][value]", columnIndex)) );

                        _params.put("_search", "%" + s + "%");
                    }

                }
            }

            if ( gSearch.length() > 0) {
                sFilter.append( "(" ).append( gSearch ).append( ")" );
            }

            if( columnSearch.length() > 0 ){
                if( sFilter.length() > 0 ){
                    sFilter.append( "OR (" ).append( columnSearch ).append( ")" );
                }
            }
        }

        System.err.println("============== SETTING FILTER ===============");

        // Get the filter
        return sFilter;
    }

    /**
     * Omit the aggregate functions as you set the filter
     *
     * @param column
     * @return Boolean
     */
    private boolean checkColumn(String column) {
        String temp = column.toUpperCase().replace(" ", "");
        return !(temp.startsWith("MIN") || temp.startsWith("MAX(") || temp.startsWith("SUM(") || temp.startsWith("COUNT("));
    }

    /**
     * Constructs the ORDER BY clause for the query
     *
     * @return String
     */
    private StringBuilder setOrder() {
        System.err.println("============= SETTING ORDER ======================");
        Map<String, String[]> map =_request.getParameterMap();
        Map<String, Object> orderColumnsMap = map.entrySet().stream()
                .filter( p -> p.getKey().startsWith("order")  )
                .collect( Collectors.toMap( p -> p.getKey(), p -> {
                    String[] values = p.getValue();
                    String actualValue = values[0];
                    return actualValue;
                }) );

        int noOfColumns = orderColumnsMap.size();
        System.err.println("noOfColumns: "+ noOfColumns);

        //Build the order
        StringBuilder sOrder = new StringBuilder();
        int columnIndex;
        String orderKey = "";
        for (Map.Entry<String, Object> entry : orderColumnsMap.entrySet()) {

            //Retrieve the key
            orderKey = entry.getKey();
            if( orderKey.endsWith("[column]") ){
                //Retrieve the column index
                columnIndex = Integer.parseInt( orderKey.replaceAll("[^\\d]", "") );
                System.err.println("columnIndex:" + columnIndex);

                //Check if this column is orderable
                if( _request.getParameter( String.format("columns[%d][orderable]", columnIndex)).equals( "true" )){
                    //Build the query
                    sOrder.append(_columnNames.get( columnIndex )).append(" ");
                    sOrder.append((_request.getParameter(String.format("order[%d][dir]", columnIndex) )
                            .equals("asc")) ? "ASC" : "DESC");
                }
            }
        }

        System.err.println(" sOrder: "+ sOrder.toString() );
        System.err.println("============= SETTING ORDER ======================");

        // The order query
        return sOrder;
    }
}
