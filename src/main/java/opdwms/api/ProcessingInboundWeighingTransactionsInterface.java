package opdwms.api;

import opdwms.api.models.WeighbridgeTransactions;

import java.util.Map;

public interface ProcessingInboundWeighingTransactionsInterface {

    public Map<String, Object> saveTransaction(WeighbridgeTransactions transaction);
}
