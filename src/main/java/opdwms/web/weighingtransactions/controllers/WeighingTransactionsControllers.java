package opdwms.web.weighingtransactions.controllers;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import opdwms.core.template.AjaxUtils;
import opdwms.core.template.View;
import opdwms.core.template.datatables.DatatablesInterface;
import opdwms.web.weighingtransactions.entities.TaggingClearanceReport;
import opdwms.web.weighingtransactions.entities.TaggingTransactions;
import opdwms.web.weighingtransactions.entities.WeighingTransactions;
import opdwms.web.weighingtransactions.forms.TaggingClearanceReportForm;
import opdwms.web.weighingtransactions.forms.WeighingTransactionsForm;
import opdwms.web.weighingtransactions.repositories.TaggingClearanceReportRepository;
import opdwms.web.weighingtransactions.repositories.TaggingTransactionsRepository;
import opdwms.web.weighingtransactions.repositories.WeighbridgeTransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class WeighingTransactionsControllers {

    private DatatablesInterface datatable;
    private TaggingTransactionsRepository taggingTransactionsRepository;
    private WeighbridgeTransactionsRepository weighbridgeTransactionsRepository;
    private TaggingClearanceReportRepository taggingClearanceReportRepository;
    private WeighingTransactionsForm weighingTransactionsForm;
    private TaggingClearanceReportForm taggingClearanceReportForm;

    @Autowired
    public WeighingTransactionsControllers(DatatablesInterface datatable,
                                           TaggingTransactionsRepository taggingTransactionsRepository,
                                           WeighbridgeTransactionsRepository weighbridgeTransactionsRepository,
                                           WeighingTransactionsForm weighingTransactionsForm,
                                           TaggingClearanceReportForm taggingClearanceReportForm,
                                           TaggingClearanceReportRepository taggingClearanceReportRepository) {
        this.datatable = datatable;
        this.taggingTransactionsRepository = taggingTransactionsRepository;
        this.weighbridgeTransactionsRepository = weighbridgeTransactionsRepository;
        this.weighingTransactionsForm = weighingTransactionsForm;
        this.taggingClearanceReportForm = taggingClearanceReportForm;
        this.taggingClearanceReportRepository = taggingClearanceReportRepository;
    }


    @RequestMapping("/weighing-transactions")
    public ModelAndView weighingTransaction(HttpServletRequest request) {
        View view = new View("weighing-transactions/weighing-transactions");
        // Fetch the table data
        if (AjaxUtils.isAjaxRequest(request)) {

            String action = request.getParameter("action");
            //When creating a record
            if (null != action && "fetch-record".equals(action)) {
                return view.sendJSON(fetchTicketData(request));

            } else {
                //Set-up data
                datatable.esDocument("static_station_trans")
                        .esFields("transactionDate", "status", "ticketNo", "stationName", "vehicleNo", "axleConfiguration",
                                "vehicleGVM", "operator", "operatorShift", "permitNo", "id")
                        .esDateFields("transactionDate");
                return view.sendJSON(datatable.showEsTable());
//                datatable
//                        .select("str(a.transactionDate; 'YYYY-MM-DD HH24:MI'), a.status, a.ticketNo, b.name, a.vehicleNo, ")
//                        .select(" a.axleConfiguration, a.vehicleGVM,  ")
//                        .select("a.operator, a.operatorShift, a.permitNo, a.id ")
//                        .from("WeighingTransactions a LEFT JOIN a.weighbridgeStationsLink  b ");

//                return view.sendJSON(datatable.showTable());
            }
        }
        return view.getView();
    }

    @RequestMapping(value = "/weighing-transactions/{index}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> export(
            javax.servlet.http.HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable("index") Long index) throws IOException {
        try {
            Optional<WeighingTransactions> weighbridgeTransactionsOptional = weighbridgeTransactionsRepository.findById(index);
            if (weighbridgeTransactionsOptional.isPresent()) {
                String sourceFileName = "config/jasper/WeighingTicket.jasper";

                WeighingTransactions weighingTransactions = weighbridgeTransactionsOptional.get();


                Map<String, Object> params = new HashMap<>();
                params.put("ticketDate", new SimpleDateFormat("MM-DD-YYYY").format(weighingTransactions.getTransactionDate()));
                params.put("ticketTime", new SimpleDateFormat("HH:mm a").format(weighingTransactions.getTransactionDate()));
                params.put("stationName", weighingTransactions.getWeighbridgeStationsLink().getName().toUpperCase());
                params.put("ticketRef", weighingTransactions.getTicketNo().toUpperCase());

                params.put("transporterName", "N/A");
                params.put("tripDestination", weighingTransactions.getDestination().isEmpty() ? "N/A" : weighingTransactions.getOrigin());
                params.put("tripOrigin", weighingTransactions.getOrigin().isEmpty() ? "N/A" : weighingTransactions.getOrigin());
                params.put("axleClass", weighingTransactions.getAxleConfiguration());

                params.put("driverName", "N/A");
                params.put("vehicleRegNo", weighingTransactions.getVehicleNo());
                params.put("specialLoadPermitNo", weighingTransactions.getPermitNo().equals("0") ? "N/A" : weighingTransactions.getPermitNo());
                params.put("tripCargo", weighingTransactions.getCargo().isEmpty() ? "N/A" : weighingTransactions.getCargo());

                params.put("shift", "N/A");
                params.put("operator", weighingTransactions.getOperator() == null ? "N/A" : weighingTransactions.getOperator());

                List<Map<String, String>> listItems = new ArrayList<>();
                Map<String, String> axleOne = new HashMap<>();
                axleOne.put("column2", String.valueOf(weighingTransactions.getFirstAxleLegalWeight().intValue()));
                axleOne.put("column3", String.valueOf(weighingTransactions.getFirstAxleLegalWeight().multiply(new BigDecimal("1.05")).intValue()));
                axleOne.put("column4", String.valueOf(weighingTransactions.getFirstAxleWeight().intValue()));
                axleOne.put("column5", weighingTransactions.getFirstAxleWeightExceededValue() == null ? "0" : String.valueOf(weighingTransactions.getFirstAxleWeightExceededValue().intValue()));
                axleOne.put("column6", weighingTransactions.getFirstAxleWeightExceededValue() == null ?
                        "legal" : weighingTransactions.getFirstAxleWeightExceededValue().intValue() > 0 ? "overload" : "legal");
                listItems.add(axleOne);

                Map<String, String> axleTwo = new HashMap<>();
                axleTwo.put("column2", String.valueOf(weighingTransactions.getSecondAxleLegalWeight().intValue()));
                axleTwo.put("column3", String.valueOf(weighingTransactions.getSecondAxleLegalWeight().multiply(new BigDecimal("1.05")).intValue()));
                axleTwo.put("column4", String.valueOf(weighingTransactions.getSecondAxleWeight().intValue()));
                axleTwo.put("column5", weighingTransactions.getSecondAxleWeightExceededValue() == null ? "0" : String.valueOf(weighingTransactions.getSecondAxleWeightExceededValue().intValue()));
                axleTwo.put("column6", weighingTransactions.getSecondAxleWeightExceededValue() == null ?
                        "legal" : weighingTransactions.getSecondAxleWeightExceededValue().intValue() > 0 ? "overload" : "legal");
                listItems.add(axleTwo);

                if (weighingTransactions.getThirdAxleWeight().intValue() != 0) {

                    Map<String, String> record = new HashMap<>();
                    record.put("column2", String.valueOf(weighingTransactions.getThirdAxleLegalWeight().intValue()));
                    record.put("column3", String.valueOf(weighingTransactions.getThirdAxleLegalWeight().multiply(new BigDecimal("1.05")).intValue()));
                    record.put("column4", String.valueOf(weighingTransactions.getThirdAxleWeight().intValue()));
                    record.put("column5", weighingTransactions.getThirdAxleWeightExceededValue() == null ? "0" : String.valueOf(weighingTransactions.getThirdAxleWeightExceededValue().intValue()));
                    record.put("column6", weighingTransactions.getThirdAxleWeightExceededValue() == null ?
                            "legal" : weighingTransactions.getThirdAxleWeightExceededValue().intValue() > 0 ? "overload" : "legal");
                    listItems.add(record);
                }

                if (weighingTransactions.getFourthAxleWeight().intValue() != 0) {
                    Map<String, String> record = new HashMap<>();
                    record.put("column2", String.valueOf(weighingTransactions.getFourthAxleLegalWeight().intValue()));
                    record.put("column3", String.valueOf(weighingTransactions.getFourthAxleLegalWeight().multiply(new BigDecimal("1.05")).intValue()));
                    record.put("column4", String.valueOf(weighingTransactions.getFourthAxleWeight().intValue()));
                    record.put("column5", weighingTransactions.getFourthAxleWeightExceededValue() == null ? "0" : String.valueOf(weighingTransactions.getFourthAxleWeightExceededValue().intValue()));
                    record.put("column6", weighingTransactions.getFourthAxleWeightExceededValue() == null ?
                            "legal" : weighingTransactions.getFourthAxleWeightExceededValue().intValue() > 0 ? "overload" : "legal");
                    listItems.add(record);
                }

                if (weighingTransactions.getFifthAxleWeight().intValue() != 0) {
                    Map<String, String> record = new HashMap<>();
                    record.put("column2", String.valueOf(weighingTransactions.getFifthAxleLegalWeight().intValue()));
                    record.put("column3", String.valueOf(weighingTransactions.getFifthAxleLegalWeight().multiply(new BigDecimal("1.05")).intValue()));
                    record.put("column4", String.valueOf(weighingTransactions.getFifthAxleWeight().intValue()));
                    record.put("column5", weighingTransactions.getFifthAxleWeightExceededValue() == null ? "0" : String.valueOf(weighingTransactions.getFifthAxleWeightExceededValue().intValue()));
                    record.put("column6", weighingTransactions.getFifthAxleWeightExceededValue() == null ?
                            "legal" : weighingTransactions.getFifthAxleWeightExceededValue().intValue() > 0 ? "overload" : "legal");
                    listItems.add(record);
                }

                if (weighingTransactions.getSixthAxleWeight().intValue() != 0) {
                    Map<String, String> record = new HashMap<>();
                    record.put("column2", String.valueOf(weighingTransactions.getSixthAxleWeight().intValue()));
                    record.put("column3", String.valueOf(weighingTransactions.getSixthAxleLegalWeight().multiply(new BigDecimal("1.05")).intValue()));
                    record.put("column4", String.valueOf(weighingTransactions.getSixthAxleWeight().intValue()));
                    record.put("column5", weighingTransactions.getSixthAxleWeightExceededValue() == null ? "0" : String.valueOf(weighingTransactions.getSixthAxleWeightExceededValue().intValue()));
                    record.put("column6", weighingTransactions.getSixthAxleWeightExceededValue() == null ?
                            "legal" : weighingTransactions.getSixthAxleWeightExceededValue().intValue() > 0 ? "overload" : "legal");
                    listItems.add(record);
                }

                if (weighingTransactions.getSeventhAxleWeight().intValue() != 0) {
                    Map<String, String> record = new HashMap<>();
                    record.put("column2", String.valueOf(weighingTransactions.getSeventhAxleWeight().intValue()));
                    record.put("column3", String.valueOf(weighingTransactions.getSeventhAxleLegalWeight().multiply(new BigDecimal("1.05")).intValue()));
                    record.put("column4", String.valueOf(weighingTransactions.getSeventhAxleWeight().intValue()));
                    record.put("column5", weighingTransactions.getSeventhAxleWeightExceededValue() == null ? "0" : String.valueOf(weighingTransactions.getSeventhAxleWeightExceededValue().intValue()));
                    record.put("column6", weighingTransactions.getSeventhAxleWeightExceededValue() == null ?
                            "legal" : weighingTransactions.getSeventhAxleWeightExceededValue().intValue() > 0 ? "overload" : "legal");
                    listItems.add(record);
                }

                Map<String, String> record = new HashMap<>();
                record.put("column1", "GVW");
                record.put("column2", "-");
                record.put("column3", "-");
                record.put("column4", weighingTransactions.getVehicleGVM().toString());
                record.put("column5", weighingTransactions.getGvwExceededWeight().toString());
                record.put("column6", weighingTransactions.getGvwExceededWeight() == null ?
                        "legal" : weighingTransactions.getGvwExceededWeight().intValue() > 0 ? "overload" : "legal");
                listItems.add(record);

                JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(listItems);
                params.put("ItemDataSource", itemsJRBean);

                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Disposition", "inline; filename=wms-" + weighingTransactions.getTicketNo().toUpperCase() + ".pdf");
                JasperPrint p = JasperFillManager.fillReport(sourceFileName, params, new JREmptyDataSource());
                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(p));

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                SimpleOutputStreamExporterOutput c = new SimpleOutputStreamExporterOutput(outputStream);
                exporter.setExporterOutput(c);

                exporter.exportReport();
                ByteArrayInputStream bis = new ByteArrayInputStream(outputStream.toByteArray());

                return ResponseEntity
                        .ok()
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(new InputStreamResource(bis));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(500).contentType(MediaType.APPLICATION_PDF).body(null);
    }


    private Map<String, Object> fetchTicketData(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        Optional<WeighingTransactions> weighbridgeTransactionsOptional = weighbridgeTransactionsRepository.findById(Long.valueOf(request.getParameter("index")));
        if (weighbridgeTransactionsOptional.isPresent()) {
            WeighingTransactions weighingTransactions = weighbridgeTransactionsOptional.get();
            map = weighingTransactionsForm.transformEntity(weighingTransactions);
            map.put("status", "00");
            map.put("message", "Transaction processed successfully");
            map.put("weighbridge", weighingTransactions.getWeighbridgeStationsLink().getName());
        } else {
            map.put("status", "01");
            map.put("message", "Invalid Transaction Id provided");
        }
        return map;
    }

    @RequestMapping("/tagging-open-transactions")
    public ModelAndView taggingTransaction(HttpServletRequest request) {
        View view = new View("weighing-transactions/tagging-transactions");


        String action = request.getParameter("action");
        // Fetch the table data
        if (AjaxUtils.isAjaxRequest(request)) {
            if (null != action && "fetch-record".equals(action)) {
                return view.sendJSON(fetchTaggingTicketData(request));
            } else if (null != action && "clear-tag".equals(action)) {
                return view.sendJSON(clearTaggingTransaction(request));
            } else {
                //Set-up data
                datatable
                        .select("str(a.transactionDate; 'YYYY-MM-DD HH24:MI'), a.tagReference, a.vehicleNo, a.transgression, ")
                        .select("a.taggingSystem, a.taggingScene, ")
                        .select(" a.weighbridge, a.chargedReason, a.id ")
                        .from("TaggingTransactions a ")
                        .where("a.tagStatus = :state")
                        .setParameter("state", TaggingTransactions.OPEN_TAGS);
                return view.sendJSON(datatable.showTable());
            }
        }
        return view.getView();
    }

    private Map<String, Object> clearTaggingTransaction(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        Long userId = (Long) request.getSession().getAttribute("_userNo");
        TaggingClearanceReport clearanceReport = taggingClearanceReportForm.handleRequests(request);
        clearanceReport.createdOn(userId);

        // Get tagging transaction
        Optional<TaggingTransactions> optionalTaggingTransactions = taggingTransactionsRepository.findById(Long.valueOf(request.getParameter("taggingTransactionsNo")));
        if (optionalTaggingTransactions.isPresent()) {
            TaggingTransactions taggingTransactions = optionalTaggingTransactions.get();
            // set its id
            clearanceReport.setId(taggingTransactions.getId())
            .setReferenceNo(taggingTransactions.getWeighingReference());
            // change tag status
            taggingTransactions.setTagStatus(TaggingTransactions.CLEARED_TAGS);
            // save both transactions
            taggingTransactionsRepository.save(taggingTransactions);
            taggingClearanceReportRepository.save(clearanceReport);
            map.put("status", "00");
            map.put("message", "Transaction cleared");
        } else {
            map.put("status", "01");
            map.put("message", "Invalid Transaction ID");
        }
        return map;
    }

    @RequestMapping("/tagging-cleared-transactions")
    public ModelAndView taggingClearedTransaction(HttpServletRequest request) {
        View view = new View("weighing-transactions/tagging-cleared-transactions");


        String action = request.getParameter("action");
        // Fetch the table data
        if (AjaxUtils.isAjaxRequest(request)) {
            if (null != action && "fetch-record".equals(action)) {
                return view.sendJSON(fetchTaggingTicketData(request));

            } else {
                //Set-up data
                datatable
                        .select("str(a.createdOn; 'YYYY-MM-DD HH24:MI'), c.email, a.narration, b.transgression, ")
                        .select("b.tagReference, b.vehicleNo, b.id  ")
                        .from("TaggingClearanceReport a ")
                        .from("LEFT JOIN TaggingTransactions b ON b.id = a.taggingTransactionsNo")
                        .from("LEFT JOIN Users c ON c.id = a.createdBy")
                        .setParameter("state", TaggingTransactions.CLEARED_TAGS);

                return view.sendJSON(datatable.showTable());
            }
        }
        return view.getView();
    }


    private Map<String, Object> fetchTaggingTicketData(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();

        Optional<TaggingTransactions> taggingTransactionsRepositoryById = taggingTransactionsRepository.findById(Long.valueOf(request.getParameter("index")));
        if (taggingTransactionsRepositoryById.isPresent()) {
            map.put("status", "00");
            map.put("message", "Transaction successful");
            map.put("data", taggingTransactionsRepositoryById.get());
        } else {
            map.put("status", "01");
            map.put("message", "Invalid Transaction ID requested");
        }
        return map;
    }


    @RequestMapping("/hswim-transactions")
    public ModelAndView HSWIMTransaction(HttpServletRequest request) {
        View view = new View("weighing-transactions/hswim-transactions");
        // Fetch the table data
        if (AjaxUtils.isAjaxRequest(request)) {
            //Set-up data
            datatable
                    .select("str(a.transactionDate; 'YYYY-MM-DD HH24:MI'), a.ticketNo, a.stationName,  a.vehicleSpeed, ")
                    .select(" a.vehicleLength, a.axleCount, ")
                    .select("a.axleOne, a.axleTwo, a.axleThree, a.axleFour, a.axleFive, a.axleSix, a.axleSeven ")
                    .from("HSWIMTransaction a");

            return view.sendJSON(datatable.showTable());
        }
        return view.getView();
    }
}
