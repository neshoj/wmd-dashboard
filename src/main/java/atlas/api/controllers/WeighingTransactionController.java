package atlas.api.controllers;

import atlas.api.ProcessingInboundWeighingTransactionsInterface;
import atlas.api.models.WeighbridgeTransactions;
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
    @PostMapping(path = "/weighing-transaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Map<String, Object> searchCustomerAccount(@RequestBody WeighbridgeTransactions request) {
        return processingInboundWeighingTransactionsInterface.saveTransaction(request);
    }

}
