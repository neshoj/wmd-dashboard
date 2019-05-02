package opdwms.core.export;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * The utility used to export a excel file from a given set of parameters
 *
 * @author Ignatius
 * @version 1.0.0
 * @category Export
 * @package Dev
 * @since Nov 05, 2018
 */
@Transactional(readOnly = true)
@Service("pdfGridExportService")
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ExportFileGenerator extends AbstractExport {

    /**
     * Generate a csv file from the HQL passed to the object
     *
     * @throws IOException
     */
    @Override
    public void generateDoc(String jasperReportTemplate, String format) throws IOException {
        // If the response or the hql has not been defined, end here
        checkParams("hql");

        // Set the appropriate headers
        setHeaders(_fileName, format);

        Map<String, Object> params = new HashMap<>();
        params.put("date", _reportMetaData.getDate());
        params.put("stationName", _reportMetaData.getStationName().toUpperCase());
        params.put("reportTitle", _reportMetaData.getReportTitle().toUpperCase());
        params.put("reportFilter", (null == _bag.get("actionId")) ? "ALL" : getReportFilter((String) _bag.get("actionId")).toUpperCase());
        params.put("period", (null == _bag.get("startDate")) ? "CURRENT YEAR" : (_bag.get("startDate")) + " to " + _bag.get("endDate"));
        List list = resultSet().list();
        List<Map<String, String>> listItems = new ArrayList<>();
        Iterator<Object[]> it = list.iterator();

        try {
            String sourceFileName = "";
            switch (jasperReportTemplate) {
                case "DailyWeighingReport":
                    if (_columns.length == 3) {
                        params = jasperTemplateColumnPopulator(params, listItems, it);
                        sourceFileName = "config/jasper/DailyWeighingReport.jasper";
                    } else {
                        params = jasperTemplateColumnPopulator(params, listItems, it);
                        sourceFileName = "config/jasper/TransactionReport.jasper";
                    }
                    break;
                case "DailyTaggingReport":
                    if (_columns.length == 2) {
                        params = jasperTemplateColumnPopulator(params, listItems, it);
                        sourceFileName = "config/jasper/DailyTaggingReport.jasper";
                    } else {
                        params = jasperTemplateColumnPopulator(params, listItems, it);
                        sourceFileName = "config/jasper/TaggingTransactionReport.jasper";
                    }
                    break;
            }

            JasperPrint p = JasperFillManager.fillReport(sourceFileName, params, new JREmptyDataSource());
            BufferedOutputStream ostream = new BufferedOutputStream(_response.getOutputStream());
            SimpleOutputStreamExporterOutput c = new SimpleOutputStreamExporterOutput(ostream);

            switch (format.toLowerCase()) {
                case "pdf":
                    JRPdfExporter pdfExporter = new JRPdfExporter();
                    pdfExporter.setExporterInput(new SimpleExporterInput(p));
                    pdfExporter.setExporterOutput(c);
                    pdfExporter.exportReport();
                    break;
                case "xlsx":
                    JRXlsxExporter excelExporter = new JRXlsxExporter();
                    excelExporter.setExporterInput(new SimpleExporterInput(p));
                    excelExporter.setExporterOutput(c);
                    excelExporter.exportReport();
                    break;
            }

        } catch (Exception e) {
            System.out.println("e = " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Create string array based on the number of columns the request has
     *
     * @param it
     * @return
     */
    private String[] rowArraySplitter(Iterator<Object[]> it) {
        Object[] dataRow;
        int i = 0, length = _columns.length;
        dataRow = it.next();
        String[] cell = new String[length];
        i = 0;
        for (Object col : dataRow) {
            if (i < length) {
                cell[i] = ((null != col) ? col.toString() : null);
            }
            i++;
        }
        return cell;
    }

    /**
     * Dynamically populate table data on the templates
     * @param params
     * @param listItems
     * @param it
     * @return
     */
    private Map<String, Object> jasperTemplateColumnPopulator(Map<String, Object> params, List<Map<String, String>> listItems, Iterator<Object[]> it) {
        while (it.hasNext()) {
            String[] cell = rowArraySplitter(it);
            Map<String, String> record = new HashMap<>();
            for (int i = 0; i < cell.length; i++) {
                record.put("column" + (i + 1), cell[i] == null ? "-" : cell[i]);
            }
            listItems.add(record);
        }

        JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(listItems);
        params.put("ItemDataSource", itemsJRBean);
        return params;
    }

    private String getReportFilter(String actionId) {
        switch (actionId) {
            case "1":
                return "Overload";
            case "2":
                return "Within Load Limit";
        }
        return "ALL";
    }

    /**
     * Generate the headers as required
     *
     * @param fileName
     */
    protected void setHeaders(String fileName, String extension) {
        _response.setContentType("application/pdf;charset=utf-8");
        _response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "." + extension + "\"");
    }

}
