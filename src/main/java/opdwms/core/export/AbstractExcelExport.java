/*
 * The MIT License
 *
 * Copyright 2016 Ken Gichia.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package opdwms.core.export;

import org.hibernate.Query;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractExcelExport implements ExportExcelInterface {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    private String _query;
    private boolean _nativeSql = false;
    protected String[] _columns;
    protected String _fileName = "export";
    protected HttpServletResponse _response;
    
    private Map<String, Object> _bag = new HashMap<String, Object>();
    private Map<String, Collection> _listParams = new HashMap<String, Collection>();
    
    /**
     * Set the filename to use
     * 
     * @param   response
     * @return  ExportInterface
     */
    @Override
    public ExportExcelInterface setResponse(HttpServletResponse response) {
        _response = ( null != response ) ? response: null;
        return this;
    }
    
    /**
     * Set the filename to use
     * 
     * @param   fileName
     * @return  ExportInterface
     */
    @Override
    public ExportExcelInterface setFileName(String fileName) {
        _fileName = ( null==fileName || fileName.isEmpty() ) ? "csv-export" : fileName;
        return this;
    }
    
    /**
     * Set the columns to use in the export file title
     * 
     * @param   columns
     * @return  ExportInterface
     */
    @Override
    public ExportExcelInterface setColumns(String[] columns) {
        _columns = columns;
        return this;
    }
    
    /**
     * Set the query string to use to generate the export document
     * 
     * @param   query
     * @return  ExportInterface
     */
    @Override
    public ExportExcelInterface setQuery(String query) {
        _query = query;
        return this;
    }
    
    /**
     * Indicate that the query string supplied is a hibernate query or native 
     * sql
     * 
     * @param   flag
     * @return  ExportInterface
     */
    @Override
    public ExportExcelInterface nativeSQL(boolean flag) {
        _nativeSql = flag;
        return this;
    }
    
    /**
     * Set auxiliary parameters that will be used by the export utility
     * 
     * @param   key
     * @param   value
     * @return  ExportInterface
     */
    @Override
    public ExportExcelInterface setParameter(String key, Object value) {
        _bag.put(key, value);
        return this;
    }
    
    /**
     * Set auxiliary parameters that will be used by the export utility
     * 
     * @param   key
     * @param   value
     * @return  ExportInterface
     */
    @Override
    public ExportExcelInterface setParameterList(String key, Collection value) {
        _listParams.put(key, value);
        return this;
    }
    
    /**
     * Called to build the query per the parameters defined if any
     * 
     * @return  Query
     */
    protected Query resultSet() {
        Session session = entityManager.unwrap( Session.class );
        Query q = _nativeSql ? session.createSQLQuery(_query): session.createQuery(_query);
        
        // Set the parameters needed
        if ( !_bag.isEmpty() ) {
            for (Map.Entry<String, Object> p : _bag.entrySet()) {
                try {
                    q.setParameter(p.getKey(), p.getValue());
                } 
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        
        if ( !_listParams.isEmpty() ) {
            for (Map.Entry<String, Collection> p : _listParams.entrySet()) {
                try {
                    q.setParameterList(p.getKey(), p.getValue());
                } 
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        
        // The query object
        return q;
    }
    
    /**
     * Check whether all the required parameters have been defined
     * 
     * @param   setting
     * @throws  IOException
     */
    protected void checkParams(String setting) throws IOException {
        // The response object is not valid
        if ( null == _response ) 
            throw new IOException("Use the setResponse method to define a valid response object.");
        
        // Check if the hql is valid
        if ( "hql".equals(setting) && (null == _query || _query.isEmpty()) )
            throw new IOException("Use the setQuery method to define a valid HQL/SQL statement.");
    }
}
