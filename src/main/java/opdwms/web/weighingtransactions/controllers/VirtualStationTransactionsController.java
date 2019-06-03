package opdwms.web.weighingtransactions.controllers;

import opdwms.core.template.AjaxUtils;
import opdwms.core.template.View;
import opdwms.core.template.datatables.DatatablesInterface;
import opdwms.web.weighingtransactions.entities.VirtualStationTransactions;
import opdwms.web.weighingtransactions.forms.VirtualStationWeighingTransactionsForm;
import opdwms.web.weighingtransactions.repositories.VirtualStationTransactionRepository;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class VirtualStationTransactionsController {
    private DatatablesInterface datatable;
    private VirtualStationTransactionRepository entityRepository;
    private VirtualStationWeighingTransactionsForm entityForm;

    @Autowired
    private VirtualStationTransactionsController(
            DatatablesInterface datatable,
            VirtualStationTransactionRepository entityRepository,
            VirtualStationWeighingTransactionsForm entityForm

    ) {
        this.datatable = datatable;
        this.entityRepository = entityRepository;
        this.entityForm = entityForm;
    }

    @RequestMapping("/virtual-station-transactions")
    public ModelAndView weighingTransaction(HttpServletRequest request) {
        View view = new View("weighing-transactions/virtual-station-transactions");
        // Fetch the table data
        if (AjaxUtils.isAjaxRequest(request)) {

            String action = request.getParameter("action");
            //When creating a record
            if (null != action && "fetch-record".equals(action)) {
                return view.sendJSON(fetchDetailedTransactionData(request));

            } else {
                //Set-up data
                datatable
                        .select("str(a.dateTime; 'YYYY-MM-DD HH24:MI'), a.flag, a.virtualStation, " +
                                "a.frontPlate, a.totalWeight, a.axleConfiguration, a.velocity, a.id ")
                        .from("VirtualStationTransactions a");

                return view.sendJSON(datatable.showTable());
            }
        }
        return view.getView();
    }

    private Map<String, Object> fetchDetailedTransactionData(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        Optional<VirtualStationTransactions> weighbridgeTransactionsOptional = entityRepository.findById(Long.valueOf(request.getParameter("index")));
        try {
            if (weighbridgeTransactionsOptional.isPresent()) {
                VirtualStationTransactions weighingTransactions = weighbridgeTransactionsOptional.get();
                map = entityForm.transformEntity(weighingTransactions);
                if (weighingTransactions.getFrontPlateBinaryImage() != null) {
                    Blob blob = weighingTransactions.getFrontPlateBinaryImage();
                    map.put("frontPlateBinaryImage", generateBase64ImageFromBlob(blob));
                }
                if (weighingTransactions.getDetailImage() != null) {
                    Blob blob = weighingTransactions.getDetailImage();
                    map.put("detailImage", generateBase64ImageFromBlob(blob));
                }
                if (weighingTransactions.getBackplateBinaryImage() != null) {
                    Blob blob = weighingTransactions.getBackplateBinaryImage();
                    map.put("backPlateBinaryImage", generateBase64ImageFromBlob(blob));
                }
                if (weighingTransactions.getDetailImageBack() != null) {
                    Blob blob = weighingTransactions.getDetailImageBack();
                    map.put("detailImageBack", generateBase64ImageFromBlob(blob));
                }
                map.put("status", "00");
                map.put("message", "Transaction processed successfully");
            } else {
                map.put("status", "01");
                map.put("message", "Invalid Transaction Id provided");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Convert blob to base64 image
     *
     * @param blob
     * @return
     * @throws SQLException
     * @throws IOException
     */
    private String generateBase64ImageFromBlob(Blob blob) throws SQLException, IOException {
        InputStream inputStream = blob.getBinaryStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead = -1;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }


    @RequestMapping("/virtual-station-tagging-transactions")
    public ModelAndView virtualStationTaggingTransactions(HttpServletRequest request) {
        View view = new View("weighing-transactions/virtual-station-tagging-transactions");
        // Fetch the table data
        if (AjaxUtils.isAjaxRequest(request)) {

            String action = request.getParameter("action");
            //When creating a record
            if (null != action && "fetch-record".equals(action)) {
                return view.sendJSON(fetchDetailedTransactionData(request));

            } else {
                //Set-up data
                datatable
                        .select("str(a.dateTime; 'YYYY-MM-DD HH24:MI'), a.flag, a.virtualStation, " +
                                "a.frontPlate, a.totalWeight, a.axleConfiguration, a.velocity, a.id ")
                        .from("VirtualStationTransactions a ")
                        .where("a.flag = :flag")
                        .setParameter("flag", VirtualStationTransactions.ABOVE_TOLERABLE_OVERLOAD);

                return view.sendJSON(datatable.showTable());
            }
        }
        return view.getView();
    }
}
