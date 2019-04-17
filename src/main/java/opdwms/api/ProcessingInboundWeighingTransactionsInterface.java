package opdwms.api;

import opdwms.api.models.HSWIMTransactionRequest;
import opdwms.api.models.TaggingTransactionsRequest;
import opdwms.api.models.WeighbridgeTransactionsRequest;

import java.util.Map;

public interface ProcessingInboundWeighingTransactionsInterface {
    public Map<String, Object> saveRawWeighingTransaction(WeighbridgeTransactionsRequest transaction);
    public Map<String, Object> saveTaggingTransaction(TaggingTransactionsRequest transaction);
    public Map<String, Object> saveHSWIMTransaction(HSWIMTransactionRequest transaction);
}
