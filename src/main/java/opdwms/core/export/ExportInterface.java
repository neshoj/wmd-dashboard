package opdwms.core.export;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * The interface that defines the utility used to export a csv file from a given
 * set of parameters
 *
 * @category    Export
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
public interface ExportInterface {
    /**
     * Set the filename to use
     * 
     * @param   response
     * @return  ExportInterface
     */
    public ExportInterface setResponse(HttpServletResponse response);
    
    /**
     * Set the filename to use
     * 
     * @param   fileName
     * @return  ExportInterface
     */
    public ExportInterface setFileName(String fileName);

    /**
     * Set report metadata. Details on top of the report
     * @param reportMetadata
     * @return
     */
    public ExportInterface setReportMetadata(ReportMetaData reportMetadata);

    /**
     * Set the columns to use in the export file title
     * 
     * @param   columns
     * @return  ExportInterface
     */
    public ExportInterface setColumns(String[] columns);
    
    /**
     * Set the query string to use to generate the export document
     * 
     * @param   query
     * @return  ExportInterface
     */
    public ExportInterface setQuery(String query);
    
    /**
     * Indicate that the query string supplied is a hibernate query or native 
     * sql
     * 
     * @param   flag
     * @return  ExportInterface
     */
    public ExportInterface nativeSQL(boolean flag);
    
    /**
     * Set auxiliary parameters that will be used by the export utility
     * 
     * @param   key
     * @param   value
     * @return  ExportInterface
     */
    public ExportInterface setParameter(String key, Object value);
    
    /**
     * Set auxiliary parameters that will be used by the export utility
     * 
     * @param   key
     * @param   value
     * @return  ExportInterface
     */
    public ExportInterface setParameterList(String key, Collection value);

    
    /**
     * Generate a export document from the query passed to the object
     * 
     * @throws  IOException
     */
    public void generateDoc(String JSReportTemplate, String format) throws IOException;
}
