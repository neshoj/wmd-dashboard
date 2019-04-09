package opdwms.api;

import opdwms.api.models.WeighbridgeTransactionsRequest;

import java.util.Map;

public interface ProcessingInboundWeighingTransactionsInterface {

    public Map<String, Object> saveTransaction(WeighbridgeTransactionsRequest transaction);
}
