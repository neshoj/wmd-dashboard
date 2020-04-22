package opdwms.core.export;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * The interface that defines the utility used to export a csv file from a given
 * set of parameters
 *
 */
public interface ExportExcelInterface {
    /**
     * Set the filename to use
     * 
     * @param   response
     * @return  ExportExcelInterface
     */
    public ExportExcelInterface setResponse(HttpServletResponse response);
    
    /**
     * Set the filename to use
     * 
     * @param   fileName
     * @return  ExportExcelInterface
     */
    public ExportExcelInterface setFileName(String fileName);
    
    /**
     * Set the columns to use in the export file title
     * 
     * @param   columns
     * @return  ExportExcelInterface
     */
    public ExportExcelInterface setColumns(String[] columns);
    
    /**
     * Set the query string to use to generate the export document
     * 
     * @param   query
     * @return  ExportExcelInterface
     */
    public ExportExcelInterface setQuery(String query);
    
    /**
     * Indicate that the query string supplied is a hibernate query or native 
     * sql
     * 
     * @param   flag
     * @return  ExportExcelInterface
     */
    public ExportExcelInterface nativeSQL(boolean flag);
    
    /**
     * Set auxiliary parameters that will be used by the export utility
     * 
     * @param   key
     * @param   value
     * @return  ExportExcelInterface
     */
    public ExportExcelInterface setParameter(String key, Object value);
    
    /**
     * Set auxiliary parameters that will be used by the export utility
     * 
     * @param   key
     * @param   value
     * @return  ExportExcelInterface
     */
    public ExportExcelInterface setParameterList(String key, Collection value);
    
    /**
     * Generate the export document using the list of 
     * 
     * @param   data
     * @throws  IOException
     */
    public void generateDoc(List<String[]> data) throws IOException;
    
    /**
     * Generate a export document from the query passed to the object
     * 
     * @throws  IOException
     */
    public void generateDoc() throws IOException;
}
