package opdwms.web.weighingtransactions.services;

import opdwms.web.weighingtransactions.WeighbridgeTransactionsServiceInterface;
import opdwms.web.weighingtransactions.entities.WeighingTransactions;
import opdwms.web.weighingtransactions.repositories.WeighbridgeTransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WeighbridgeTransactionsService  implements WeighbridgeTransactionsServiceInterface {

    private WeighbridgeTransactionsRepository weighbridgeTransactionsRepository;

    @Autowired
    public WeighbridgeTransactionsService(WeighbridgeTransactionsRepository weighbridgeTransactionsRepository){
        this.weighbridgeTransactionsRepository = weighbridgeTransactionsRepository;
    }

    public WeighingTransactions saveWeighbridgeTransaction(WeighingTransactions data){
        return weighbridgeTransactionsRepository.save(data);
    }
}
