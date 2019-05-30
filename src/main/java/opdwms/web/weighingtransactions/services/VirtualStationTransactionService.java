package opdwms.web.weighingtransactions.services;

import opdwms.web.weighingtransactions.VirtualStationTransactionServiceInterface;
import opdwms.web.weighingtransactions.entities.VirtualStationTransactions;
import opdwms.web.weighingtransactions.repositories.VirtualStationTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VirtualStationTransactionService implements VirtualStationTransactionServiceInterface {
    private VirtualStationTransactionRepository virtualStationTransactionRepository;

    @Autowired
    public VirtualStationTransactionService(VirtualStationTransactionRepository virtualStationTransactionRepository){
        this.virtualStationTransactionRepository = virtualStationTransactionRepository;
    }

    @Override
    public VirtualStationTransactions saveVirtualWeighbridgeStationTransaction(VirtualStationTransactions data) {
        return null;
    }
}
