package opdwms.web.weighingtransactions.repositories;

import opdwms.web.dashboard.vm.CencusBarChartData;
import opdwms.web.weighingtransactions.entities.WeighingTransactions;
import opdwms.web.weighingtransactions.modal.LineChartData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeighbridgeTransactionsRepository extends CrudRepository<WeighingTransactions, Long> {


    @Query( "SELECT NEW opdwms.web.weighingtransactions.modal.LineChartData( DATE_FORMAT(a.transactionDate, '%Y-%m-%d') , count(a.id)) FROM WeighingTransactions a where a.status = ?1 AND a.transactionDate >= ?2  GROUP BY DATE_FORMAT(a.transactionDate, '%Y-%m-%d')")
    public List<LineChartData> fetchWeighingCountBasedOnStatusGroupedByMonthlyDate(String status, Date daysAgo);

    @Query( "SELECT count(a.id) FROM WeighingTransactions a  where  a.status = ?1")
    public Long fetchCountOfWeighingBasedOnStatus(String status);

    @Query( "SELECT NEW opdwms.web.dashboard.vm.CencusBarChartData( count(a.id), a.axleConfiguration) FROM WeighingTransactions a GROUP BY axleConfiguration ORDER BY axleConfiguration ASC")
    public List<CencusBarChartData> fetchAxleConfigurationCensus();

    @Query( "SELECT count(a.id) FROM TaggingTransactions a WHERE a.tagStatus= ?1")
    public Long fetchCountOfTaggingBasedOnStatus(String status);

    // Station specific
    @Query( "SELECT count(a.id) FROM WeighingTransactions a  where  status = ?1 AND stationCode = ?2")
    public Long fetchCountOfWeighingBasedOnStatusAndStationCode(String status, String stationCode);

}
