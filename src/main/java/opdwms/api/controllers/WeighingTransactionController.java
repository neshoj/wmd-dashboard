package opdwms.api.controllers;

import opdwms.api.ProcessingInboundWeighingTransactionsInterface;
import opdwms.api.models.HSWIMTransactionRequest;
import opdwms.api.models.TaggingTransactionsRequest;
import opdwms.api.models.WeighbridgeTransactionsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class WeighingTransactionController {
    private ProcessingInboundWeighingTransactionsInterface processingInboundWeighingTransactionsInterface;

    @Autowired
    public WeighingTransactionController(ProcessingInboundWeighingTransactionsInterface processingInboundWeighingTransactionsInterface){
        this.processingInboundWeighingTransactionsInterface = processingInboundWeighingTransactionsInterface;
    }

    /**
     * Save transaction weighing transaction
     * @param request
     * @return
     */
    @PostMapping(path = "/api/weighing-transaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Map<String, Object> processWeighingRequestFromWeighbridgeStations(@RequestBody WeighbridgeTransactionsRequest request) {
        System.err.println("Request = " + request.toString());
        return processingInboundWeighingTransactionsInterface.saveRawWeighingTransaction(request);
    }

    @PostMapping(path = "/api/tag-transaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Map<String, Object> processTaggingReportFromWeighbridgeStations(@RequestBody TaggingTransactionsRequest request) {
        System.err.println("Tag Request = " + request.toString());
        return processingInboundWeighingTransactionsInterface.saveTaggingTransaction(request);
    }

    @PostMapping(path = "/api/hswim-transaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Map<String, Object> processHSWIMTransactionFromWeighbridgeStations(@RequestBody HSWIMTransactionRequest request) {
        System.err.println("HSWIM Request = " + request.toString());
        return processingInboundWeighingTransactionsInterface.saveHSWIMTransaction(request);
    }
}
