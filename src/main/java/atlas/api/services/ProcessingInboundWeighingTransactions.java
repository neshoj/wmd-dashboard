package atlas.api.services;

import atlas.api.ProcessingInboundWeighingTransactionsInterface;
import atlas.api.models.WeighbridgeTransactions;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProcessingInboundWeighingTransactions  implements ProcessingInboundWeighingTransactionsInterface {
    @Override
    public Map<String, Object> saveTransaction(WeighbridgeTransactions transaction) {
        return null;
    }
}
