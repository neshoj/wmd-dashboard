package opdwms.api.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import opdwms.api.ProcessingInboundWeighingTransactionsInterface;
import opdwms.api.models.HSWIMTransactionRequest;
import opdwms.api.models.TaggingTransactionsRequest;
import opdwms.api.models.WeighbridgeTransactionsRequest;
import opdwms.web.weighbridgestations.WeighbridgeStationsServiceInterface;
import opdwms.web.weighbridgestations.entities.WeighbridgeStations;
import opdwms.web.weighingtransactions.WeighbridgeTransactionsServiceInterface;
import opdwms.web.weighingtransactions.entities.HSWIMTransaction;
import opdwms.web.weighingtransactions.entities.TaggingTransactions;
import opdwms.web.weighingtransactions.entities.WeighingTransactions;
import opdwms.web.weighingtransactions.repositories.HSWIMTransactionsRepository;
import opdwms.web.weighingtransactions.repositories.TaggingTransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ProcessingInboundWeighingTransactions implements ProcessingInboundWeighingTransactionsInterface {

    private WeighbridgeStationsServiceInterface weighbridgeStationsServiceInterface;
    private WeighbridgeTransactionsServiceInterface weighbridgeTransactionsServiceInterface;
    private TaggingTransactionsRepository taggingTransactionsRepository;
    private HSWIMTransactionsRepository hswimTransactionsRepository;
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ProcessingInboundWeighingTransactions(WeighbridgeStationsServiceInterface weighbridgeStationsServiceInterface,
                                                 WeighbridgeTransactionsServiceInterface weighbridgeTransactionsServiceInterface,
                                                 SimpMessagingTemplate messagingTemplate,
                                                 HSWIMTransactionsRepository hswimTransactionsRepository,
                                                 TaggingTransactionsRepository taggingTransactionsRepository) {
        this.weighbridgeStationsServiceInterface = weighbridgeStationsServiceInterface;
        this.weighbridgeTransactionsServiceInterface = weighbridgeTransactionsServiceInterface;
        this.taggingTransactionsRepository = taggingTransactionsRepository;
        this.hswimTransactionsRepository = hswimTransactionsRepository;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Save the transaction under the registered weighbridge
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> saveRawWeighingTransaction(WeighbridgeTransactionsRequest request) {
        Map<String, Object> results = new HashMap<>();
        Optional<WeighbridgeStations> optionalWBSByCode = weighbridgeStationsServiceInterface.findByWBSCode(request.getStationCode());

        if (optionalWBSByCode.isPresent()) {
            try {
                WeighingTransactions transaction = new WeighingTransactions()
                        .setTicketNo(request.getTicketNo())
                        .setStationCode(request.getStationCode())
                        .setTransactionDate(request.getTransactionDate())
                        .setVehicleNo(request.getVehicleNo())
                        .setAxleConfiguration(request.getAxleConfiguration())

                        .setVehicleGVM(new BigDecimal(request.getWBT_1_Gross()))
                        .setOperator(request.getWBT_OPERATOR())
                        .setStatus(request.getWBT_Status())
                        .setDirection(request.getWBT_direction())
                        .setWbtbu(request.getWBT_BU())
                        .setOperatorShift(request.getWBT_Shift())

                        .setOrigin(request.getOrigin())
                        .setDestination(request.getDestination())
                        .setCargo(request.getCargo())
                        .setActionTaken(request.getActionTaken())
                        .setPermitNo(request.getPermitNo() == null ? "0" : request.getPermitNo().toString())
                        .setCreatedOn(new Date())
                        .setWeighbridgeNo(optionalWBSByCode.get().getId())

                        .setFirstAxleGrouping(request.getFirstAxleGrouping())
                        .setFirstAxleLegalWeight(new BigDecimal(request.getFirstAxleLegalWeight().toString()))
                        .setFirstAxleWeight(new BigDecimal(request.getFirstAxleWeight()))
                        .setFirstAxleType(request.getFirstAxleType())

                        .setSecondAxleGrouping(request.getSecondAxleGrouping())
                        .setSecondAxleLegalWeight(new BigDecimal(request.getSecondAxleLegalWeight().toString()))
                        .setSecondAxleWeight(new BigDecimal(request.getSecondAxleWeight()))
                        .setSecondAxleType(request.getSecondAxleType())

                        .setThirdAxleGrouping(request.getThirdAxleGrouping())
                        .setThirdAxleLegalWeight(new BigDecimal(
                                request.getThirdAxleLegalWeight() == null ? 0 : request.getThirdAxleLegalWeight()
                        ))
                        .setThirdAxleWeight(new BigDecimal(request.getThirdAxleWeight() == null ? 0 : request.getThirdAxleWeight()))
                        .setThirdAxleType(request.getThirdAxleType())


                        .setFourthAxleGrouping(request.getFourthAxleGrouping())
                        .setFourthAxleLegalWeight(new BigDecimal(
                                request.getFourthAxleLegalWeight() == null ? 0 : request.getFourthAxleLegalWeight()
                        ))
                        .setFourthAxleWeight(new BigDecimal(request.getFourthAxleWeight() == null ? 0 : request.getFourthAxleWeight()))
                        .setFourthAxleType(request.getFourthAxleType())


                        .setFifthAxleGrouping(request.getFifthAxleGrouping())
                        .setFifthAxleLegalWeight(new BigDecimal(
                                request.getFifthAxleLegalWeight() == null ? 0 : request.getFifthAxleLegalWeight()
                        ))
                        .setFifthAxleWeight(new BigDecimal(request.getFifthAxleWeight()))
                        .setFifthAxleType(request.getFifthAxleType())


                        .setSixthAxleGrouping(request.getSixthAxleGrouping())
                        .setSixthAxleLegalWeight(new BigDecimal(
                                request.getSixthAxleLegalWeight() == null ? 0 : request.getSixthAxleLegalWeight()
                        ))
                        .setSixthAxleWeight(new BigDecimal(request.getSixthAxleWeight() == null ? 0 : request.getSixthAxleWeight()))
                        .setSixthAxleType(request.getSixthAxleType())


                        .setSeventhAxleGrouping(request.getSeventhAxleGrouping())
                        .setSeventhAxleLegalWeight(new BigDecimal(
                                request.getSeventhAxleLegalWeight() == null ? 0 : request.getSeventhAxleLegalWeight()
                        ))
                        .setSeventhAxleWeight(new BigDecimal(
                                request.getSeventhAxleWeight() == null ? 0 : request.getSeventhAxleWeight()
                        ))
                        .setSeventhAxleType(request.getSeventhAxleType());

                WeighingTransactions weighbridgeStations = weighbridgeTransactionsServiceInterface.saveWeighbridgeTransaction(transaction);
                results.put("status", "00");
                results.put("message", "Transaction saved successfully");

                // Send to live review topic
                messagingTemplate.convertAndSend("/topic/weighing-transactions", new ObjectMapper().writeValueAsString(weighbridgeStations));
            } catch (Exception e) {
                System.out.println("e.getMessage() = " + e.getMessage());
            }
        } else {
            results.put("status", "01");
            results.put("message", "Invalid or Unknown Station Code");
        }

        return results;
    }

    @Override
    public Map<String, Object> saveTaggingTransaction(TaggingTransactionsRequest request) {
        Map<String, Object> results = new HashMap<>();
        Optional<WeighbridgeStations> optionalWBSByCode = weighbridgeStationsServiceInterface.findByWBSCode(request.getSceneOfTagging());

        if (optionalWBSByCode.isPresent()) {
            try {
                TaggingTransactions transaction = new TaggingTransactions()
                        .setCreatedOn(new Date())
                        .setTransactionDate(request.getTransactionTime())
                        .setChargedReason(request.getChargedReason())
                        .setConfirmedVehicle_no(request.getConfirmedVehicleNo())
                        .setVehicleNo(request.getVehicleNo())
                        .setEvidenceReference(request.getPhotoEvidence())
                        .setEvidenceId(request.getEvidenceId())
                        .setTaggingScene(request.getSceneOfTagging())
                        .setTaggingSystem(request.getTaggingSystemUsed())
                        .setTagOnChargeAmount(new BigDecimal(request.getChargedAmount() == null ? 0 : request.getChargedAmount()))
                        .setTagReference(request.getTagReference())
                        .setTagStatus(String.valueOf(request.getTagFlag()))
                        .setTagType(request.getTagType())
                        .setTransgression(request.getTransgression())
                        .setWeighbridge(request.getWeighbridge())
                        .setWeighbridgeNo(optionalWBSByCode.get().getId())
                        .setWeighingReference(request.getWeighingReference());

                TaggingTransactions weighbridgeStations = taggingTransactionsRepository.save(transaction);
                results.put("status", "00");
                results.put("message", "Transaction saved successfully");

                // Send to live review topic
                messagingTemplate.convertAndSend("/topic/tags-transactions", new ObjectMapper().writeValueAsString(weighbridgeStations));
            } catch (Exception e) {
                System.out.println("e.getMessage() = " + e.getMessage());
            }
        } else {
            results.put("status", "01");
            results.put("message", "Invalid or Unknown Station Code");
        }

        return results;
    }

    @Override
    public Map<String, Object> saveHSWIMTransaction(HSWIMTransactionRequest request) {
        Map<String, Object> results = new HashMap<>();

            try {
                HSWIMTransaction transaction = new HSWIMTransaction()
                        .setTransactionDate(request.getTransactionDate())
                        .setAxleCount(request.getAxleCount())
                        .setStationName(request.getStationName())
                        .setVehicleSpeed(request.getVehicleSpeed())
                        .setVehicleLength(request.getVehicleLength())
                        .setTicketNo(request.getTicketNo())
                        .setAxleOne(request.getAxleOne())
                        .setAxleTwo(request.getAxleTwo())
                        .setAxleThree(request.getAxleThree())
                        .setAxleFour(request.getAxleFour())
                        .setAxleFive(request.getAxleFive())
                        .setAxleSix(request.getAxleSix())
                        .setAxleSeven(request.getAxleSeven());

                HSWIMTransaction hswimTransaction = hswimTransactionsRepository.save(transaction);

                results.put("status", "00");
                results.put("message", "Transaction saved successfully");

            } catch (Exception e) {
                System.out.println("e.getMessage() = " + e.getMessage());
            }


        return results;
    }
}
