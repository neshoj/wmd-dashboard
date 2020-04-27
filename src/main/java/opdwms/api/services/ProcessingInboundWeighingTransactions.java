package opdwms.api.services;

import opdwms.api.ProcessingInboundWeighingTransactionsInterface;
import opdwms.api.models.HSWIMTransactionRequest;
import opdwms.api.models.TaggingTransactionsRequest;
import opdwms.api.models.WeighbridgeTransactionsRequest;
import opdwms.web.axleclassification.entities.AxleClassification;
import opdwms.web.axleclassification.repository.AxleClassificationRepository;
import opdwms.web.configs.entities.AppSettings;
import opdwms.web.configs.repository.SettingsRepository;
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
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ProcessingInboundWeighingTransactions
    implements ProcessingInboundWeighingTransactionsInterface {

  private static final String PROCESSED_SUCCESSFULLY = "1";
  private static final String PROCESSED_FAILED = "0";
  public static final String GVM_WITHIN = "0";
  public static final String GVM_WITHIN_PERMISSIBLE = "1";
  public static final String GVM_OVERLOAD = "2";

  public final String AXLE_WEIGHT_IS_BELOW_ALLOWED_WEIGHT = "0";
  public final String AXLE_WEIGHT_OVERLOAD_WITHIN_TOLERANCE = "1";
  public final String AXLE_WEIGHT_OVERLOAD_ABOVE_TOLERANCE = "2";

  private WeighbridgeStationsServiceInterface weighbridgeStationsServiceInterface;
  private WeighbridgeTransactionsServiceInterface weighbridgeTransactionsServiceInterface;
  private TaggingTransactionsRepository taggingTransactionsRepository;
  private HSWIMTransactionsRepository hswimTransactionsRepository;
  private AxleClassificationRepository axleClassificationRepository;
  private SettingsRepository settingsRepository;
  private SimpMessagingTemplate messagingTemplate;

  @Autowired
  public ProcessingInboundWeighingTransactions(
      WeighbridgeStationsServiceInterface weighbridgeStationsServiceInterface,
      WeighbridgeTransactionsServiceInterface weighbridgeTransactionsServiceInterface,
      SimpMessagingTemplate messagingTemplate,
      HSWIMTransactionsRepository hswimTransactionsRepository,
      TaggingTransactionsRepository taggingTransactionsRepository,
      AxleClassificationRepository axleClassificationRepository,
      SettingsRepository settingsRepository) {
    this.weighbridgeStationsServiceInterface = weighbridgeStationsServiceInterface;
    this.weighbridgeTransactionsServiceInterface = weighbridgeTransactionsServiceInterface;
    this.taggingTransactionsRepository = taggingTransactionsRepository;
    this.hswimTransactionsRepository = hswimTransactionsRepository;
    this.messagingTemplate = messagingTemplate;
    this.axleClassificationRepository = axleClassificationRepository;
    this.settingsRepository = settingsRepository;
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
    Optional<WeighbridgeStations> optionalWBSByCode =
        weighbridgeStationsServiceInterface.findByWBSId(request.getStationCode());

    if (optionalWBSByCode.isPresent()) {
      try {
        WeighingTransactions transaction =
            new WeighingTransactions()
                .setTicketNo(request.getTicketNo())
                .setStationCode(request.getStationCode())
                .setTransactionDate(request.getTransactionDate())
                .setVehicleNo(request.getVehicleNo())
                .setAxleConfiguration(request.getAxleConfiguration())
                .setVehicleGVM(new BigDecimal(request.getWBT_1_Gross()))
                .setOperator(request.getWBT_OPERATOR())
                .setOrigin(request.getOrigin())
                .setDestination(request.getDestination())
                .setCargo(request.getCargo())
                .setActionTaken(request.getActionTaken())
                .setPermitNo(request.getPermitNo() == null ? "0" : request.getPermitNo().toString())
                .setCreatedOn(new Date())
                .setWeighbridgeNo(optionalWBSByCode.get().getId())
                .setFirstAxleLegalWeight(
                    new BigDecimal(request.getFirstAxleLegalWeight().toString()))
                .setFirstAxleWeight(new BigDecimal(request.getFirstAxleWeight()))
                .setSecondAxleLegalWeight(
                    new BigDecimal(request.getSecondAxleLegalWeight().toString()))
                .setSecondAxleWeight(new BigDecimal(request.getSecondAxleWeight()))
                .setThirdAxleLegalWeight(
                    new BigDecimal(
                        request.getThirdAxleLegalWeight() == null
                            ? 0
                            : request.getThirdAxleLegalWeight()))
                .setThirdAxleWeight(
                    new BigDecimal(
                        request.getThirdAxleWeight() == null ? 0 : request.getThirdAxleWeight()))
                .setFourthAxleLegalWeight(
                    new BigDecimal(
                        request.getFourthAxleLegalWeight() == null
                            ? 0
                            : request.getFourthAxleLegalWeight()))
                .setFourthAxleWeight(
                    new BigDecimal(
                        request.getFourthAxleWeight() == null ? 0 : request.getFourthAxleWeight()))
                .setFifthAxleLegalWeight(
                    new BigDecimal(
                        request.getFifthAxleLegalWeight() == null
                            ? 0
                            : request.getFifthAxleLegalWeight()))
                .setFifthAxleWeight(new BigDecimal(request.getFifthAxleWeight()))
                .setSixthAxleLegalWeight(
                    new BigDecimal(
                        request.getSixthAxleLegalWeight() == null
                            ? 0
                            : request.getSixthAxleLegalWeight()))
                .setSixthAxleWeight(
                    new BigDecimal(
                        request.getSixthAxleWeight() == null ? 0 : request.getSixthAxleWeight()))
                .setSeventhAxleLegalWeight(
                    new BigDecimal(
                        request.getSeventhAxleLegalWeight() == null
                            ? 0
                            : request.getSeventhAxleLegalWeight()))
                .setSeventhAxleWeight(
                    new BigDecimal(
                        request.getSeventhAxleWeight() == null
                            ? 0
                            : request.getSeventhAxleWeight()));

        results = performOverloadCalculations(transaction);
        // Send to live review topic
        // messagingTemplate.convertAndSend("/topic/weighing-transactions", new
        // ObjectMapper().writeValueAsString(weighbridgeStations));
      } catch (Exception e) {
        System.out.println("e.getMessage() = " + e.getMessage());
      }
    } else {
      results.put("status", "01");
      results.put("message", "Invalid or Unknown Station Code");
    }

    return results;
  }

  private Map<String, Object> performOverloadCalculations(WeighingTransactions transaction) {
    Map<String, Object> results = new HashMap<>();
    // Fetch class information
    Optional<AxleClassification> axleClassificationRepositoryByAxleCode =
        axleClassificationRepository.findByAxleCode(
            transaction.getAxleConfiguration().trim().toUpperCase().replaceAll("\\s", ""));

    if (axleClassificationRepositoryByAxleCode.isPresent()) {

      AppSettings axle_weight_tolerance =
          settingsRepository.findByCode("AXLE_WEIGHT_TOLERANCE").get();
      Integer percentageTolerance = Integer.valueOf(axle_weight_tolerance.getValue());

      AxleClassification axleClassification = axleClassificationRepositoryByAxleCode.get();
      BigDecimal grossVehicleWeight = new BigDecimal(0);
      BigDecimal weighedGrossVehicleWeight = new BigDecimal(0);
      // Check for axle count
      switch (axleClassification.getAxleCount()) {
        case 2:
          transaction = handleTwoAxleRequest(transaction, axleClassification, percentageTolerance);
          grossVehicleWeight = axleClassification.getAxleOne().add(axleClassification.getAxleTwo());

          weighedGrossVehicleWeight =
              transaction.getFirstAxleWeight().add(transaction.getSecondAxleWeight());
          break;
        case 3:
          transaction =
              handleThreeAxleRequest(transaction, axleClassification, percentageTolerance);
          grossVehicleWeight =
              axleClassification
                  .getAxleOne()
                  .add(axleClassification.getAxleTwo())
                  .add(axleClassification.getAxleThree());

          weighedGrossVehicleWeight =
              transaction
                  .getFirstAxleWeight()
                  .add(transaction.getSecondAxleWeight())
                  .add(transaction.getThirdAxleWeight());
          break;
        case 4:
          transaction = handleFourAxleRequest(transaction, axleClassification, percentageTolerance);
          grossVehicleWeight =
              axleClassification
                  .getAxleOne()
                  .add(axleClassification.getAxleTwo())
                  .add(axleClassification.getAxleThree())
                  .add(axleClassification.getAxleFour());

          weighedGrossVehicleWeight =
              transaction
                  .getFirstAxleWeight()
                  .add(transaction.getSecondAxleWeight())
                  .add(transaction.getThirdAxleWeight())
                  .add(transaction.getFourthAxleWeight());
          break;
        case 5:
          transaction = handleFiveAxleRequest(transaction, axleClassification, percentageTolerance);
          grossVehicleWeight =
              axleClassification
                  .getAxleOne()
                  .add(axleClassification.getAxleTwo())
                  .add(axleClassification.getAxleThree())
                  .add(axleClassification.getAxleFour())
                  .add(axleClassification.getAxleFive());

          weighedGrossVehicleWeight =
              transaction
                  .getFirstAxleWeight()
                  .add(transaction.getSecondAxleWeight())
                  .add(transaction.getThirdAxleWeight())
                  .add(transaction.getFourthAxleWeight())
                  .add(transaction.getFifthAxleWeight());
          break;
        case 6:
          transaction = handleSixAxleRequest(transaction, axleClassification, percentageTolerance);
          grossVehicleWeight =
              axleClassification
                  .getAxleOne()
                  .add(axleClassification.getAxleTwo())
                  .add(axleClassification.getAxleThree())
                  .add(axleClassification.getAxleFour())
                  .add(axleClassification.getAxleFive())
                  .add(axleClassification.getAxleSix());

          weighedGrossVehicleWeight =
              transaction
                  .getFirstAxleWeight()
                  .add(transaction.getSecondAxleWeight())
                  .add(transaction.getThirdAxleWeight())
                  .add(transaction.getFourthAxleWeight())
                  .add(transaction.getFifthAxleWeight())
                  .add(transaction.getSixthAxleWeight());
          break;
        case 7:
          transaction =
              handleSevenAxleRequest(transaction, axleClassification, percentageTolerance);
          grossVehicleWeight =
              axleClassification
                  .getAxleOne()
                  .add(axleClassification.getAxleTwo())
                  .add(axleClassification.getAxleThree())
                  .add(axleClassification.getAxleFour())
                  .add(axleClassification.getAxleFive())
                  .add(axleClassification.getAxleSix())
                  .add(axleClassification.getAxleSeven());

          weighedGrossVehicleWeight =
              transaction
                  .getFirstAxleWeight()
                  .add(transaction.getSecondAxleWeight())
                  .add(transaction.getThirdAxleWeight())
                  .add(transaction.getFourthAxleWeight())
                  .add(transaction.getFifthAxleWeight())
                  .add(transaction.getSixthAxleWeight())
                  .add(transaction.getSeventhAxleWeight());
          break;
      }
      // Check GVM overload
      transaction
          .setGvwExceededWeight(
              calculateAxleWeightsOverload(grossVehicleWeight, weighedGrossVehicleWeight))
          .setGvwExceededPercent(
              calculateAxleWeightsPercentageOverload(
                  grossVehicleWeight, weighedGrossVehicleWeight));
      // Transaction GVW overload
      transaction
          .setStatus(transaction.getGvwExceededWeight().intValue() > 0 ? GVM_OVERLOAD : GVM_WITHIN)
          .setFlag(PROCESSED_SUCCESSFULLY);

      // Check percentage overload on axles
      if (transaction.getGvwExceededPercent().intValue() == 0) {
        transaction.setAxleWeightStatus(AXLE_WEIGHT_IS_BELOW_ALLOWED_WEIGHT);
      } else if (transaction.getGvwExceededPercent().intValue() <= percentageTolerance) {
        transaction.setAxleWeightStatus(AXLE_WEIGHT_OVERLOAD_WITHIN_TOLERANCE);
      } else {
        transaction.setAxleWeightStatus(AXLE_WEIGHT_OVERLOAD_ABOVE_TOLERANCE);
      }

      WeighingTransactions weighingTransactions =
          weighbridgeTransactionsServiceInterface.saveWeighbridgeTransaction(transaction);

      results.put("status", "00");
      results.put("message", "Transaction saved successfully");
    } else {
      results.put("status", "01");
      results.put("message", "Unknown axle classification has been used");
      transaction.setFlag(PROCESSED_FAILED);

      WeighingTransactions weighingTransactions =
          weighbridgeTransactionsServiceInterface.saveWeighbridgeTransaction(transaction);
    }

    return results;
  }

  private WeighingTransactions handleSevenAxleRequest(
      WeighingTransactions transaction,
      AxleClassification axleClassification,
      int percentageTolerance) {
    transaction = handleSixAxleRequest(transaction, axleClassification, percentageTolerance);

    BigDecimal tolerableWeightAxleSeven =
        calculateTolerableWeight(axleClassification.getAxleSix(), percentageTolerance);
    transaction.setSeventhAxleWeightExceededValue(
        calculateAxleWeightsOverload(tolerableWeightAxleSeven, transaction.getSecondAxleWeight()));
    transaction.setSeventhAxleWeightExceededPercent(
        calculateAxleWeightsPercentageOverload(
            tolerableWeightAxleSeven, transaction.getSeventhAxleWeight()));

    return transaction;
  }

  private WeighingTransactions handleSixAxleRequest(
      WeighingTransactions transaction,
      AxleClassification axleClassification,
      int percentageTolerance) {

    transaction = handleFiveAxleRequest(transaction, axleClassification, percentageTolerance);

    BigDecimal tolerableWeightAxleSix =
        calculateTolerableWeight(axleClassification.getAxleSix(), percentageTolerance);
    transaction.setSixthAxleWeightExceededValue(
        calculateAxleWeightsOverload(tolerableWeightAxleSix, transaction.getSecondAxleWeight()));
    transaction.setSixthAxleWeightExceededPercent(
        calculateAxleWeightsPercentageOverload(
            tolerableWeightAxleSix, transaction.getSixthAxleWeight()));

    return transaction;
  }

  private WeighingTransactions handleFiveAxleRequest(
      WeighingTransactions transaction,
      AxleClassification axleClassification,
      int percentageTolerance) {

    transaction = handleFourAxleRequest(transaction, axleClassification, percentageTolerance);

    BigDecimal tolerableWeightAxleFive =
        calculateTolerableWeight(axleClassification.getAxleFive(), percentageTolerance);
    transaction.setFifthAxleWeightExceededValue(
        calculateAxleWeightsOverload(tolerableWeightAxleFive, transaction.getSecondAxleWeight()));
    transaction.setFifthAxleWeightExceededPercent(
        calculateAxleWeightsPercentageOverload(
            tolerableWeightAxleFive, transaction.getFifthAxleWeight()));

    return transaction;
  }

  private WeighingTransactions handleFourAxleRequest(
      WeighingTransactions transaction,
      AxleClassification axleClassification,
      int percentageTolerance) {

    transaction = handleThreeAxleRequest(transaction, axleClassification, percentageTolerance);

    BigDecimal tolerableWeightAxleFour =
        calculateTolerableWeight(axleClassification.getAxleFour(), percentageTolerance);
    transaction.setFourthAxleWeightExceededValue(
        calculateAxleWeightsOverload(tolerableWeightAxleFour, transaction.getSecondAxleWeight()));
    transaction.setFourthAxleWeightExceededPercent(
        calculateAxleWeightsPercentageOverload(
            tolerableWeightAxleFour, transaction.getFourthAxleWeight()));

    return transaction;
  }

  private WeighingTransactions handleThreeAxleRequest(
      WeighingTransactions transaction,
      AxleClassification axleClassification,
      int percentageTolerance) {

    transaction = handleTwoAxleRequest(transaction, axleClassification, percentageTolerance);

    BigDecimal tolerableWeightAxleThree =
        calculateTolerableWeight(axleClassification.getAxleThree(), percentageTolerance);
    transaction.setThirdAxleWeightExceededValue(
        calculateAxleWeightsOverload(tolerableWeightAxleThree, transaction.getSecondAxleWeight()));
    transaction.setThirdAxleWeightExceededPercent(
        calculateAxleWeightsPercentageOverload(
            tolerableWeightAxleThree, transaction.getThirdAxleWeight()));

    return transaction;
  }

  private WeighingTransactions handleTwoAxleRequest(
      WeighingTransactions transaction,
      AxleClassification axleClassification,
      int percentageTolerance) {
    // Fetch from configuration allowed percentage overload
    // Calculate tolerable axle overload: currently set at 5% of allowed weight
    BigDecimal tolerableWeight =
        calculateTolerableWeight(axleClassification.getAxleOne(), percentageTolerance);
    transaction.setFirstAxleWeightExceededValue(
        calculateAxleWeightsOverload(tolerableWeight, transaction.getFirstAxleWeight()));
    transaction.setFirstAxleWeightExceededPercent(
        calculateAxleWeightsPercentageOverload(tolerableWeight, transaction.getFirstAxleWeight()));

    BigDecimal tolerableWeightAxleTwo =
        calculateTolerableWeight(axleClassification.getAxleTwo(), percentageTolerance);
    transaction.setSecondAxleWeightExceededValue(
        calculateAxleWeightsOverload(tolerableWeightAxleTwo, transaction.getSecondAxleWeight()));
    transaction.setSecondAxleWeightExceededPercent(
        calculateAxleWeightsPercentageOverload(
            tolerableWeightAxleTwo, transaction.getSecondAxleWeight()));

    return transaction;
  }

  /**
   * Calculate permissable percentage
   *
   * @param legalWeight
   * @param permissiblePercentage
   * @return
   */
  private BigDecimal calculateTolerableWeight(BigDecimal legalWeight, int permissiblePercentage) {
    return (legalWeight.multiply(new BigDecimal(100 + permissiblePercentage)))
        .divide(new BigDecimal(100), RoundingMode.HALF_UP);
  }

  /**
   * @param permissibleWeight Axle classification permissible weight. Includes the % tolerance
   * @param readAxleWeight Scale reading
   * @return Value of overload
   */
  private BigDecimal calculateAxleWeightsOverload(
      BigDecimal permissibleWeight, BigDecimal readAxleWeight) {
    // -1 : if value of this BigDecimal is less than that of BigDecimal object passed as parameter.
    if (permissibleWeight.compareTo(readAxleWeight) < 0) {
      // Overload: Get overload weight
      return readAxleWeight.subtract(permissibleWeight);
    }
    return new BigDecimal(0);
  }

  /**
   * @param permissibleWeight Axle classification permissible weight. Includes the % tolerance
   * @param readAxleWeight Scale reading
   * @return Percentage value of overload
   */
  private BigDecimal calculateAxleWeightsPercentageOverload(
      BigDecimal permissibleWeight, BigDecimal readAxleWeight) {
    // -1 : if value of this BigDecimal is less than that of BigDecimal object passed as parameter.
    if (permissibleWeight.compareTo(readAxleWeight) < 0) {
      // Overload: Get overload weight
      BigDecimal weightOverload = readAxleWeight.subtract(permissibleWeight);
      return (weightOverload.multiply(new BigDecimal(100)))
          .divide(permissibleWeight, RoundingMode.HALF_UP);
    }
    return new BigDecimal(0);
  }

  @Override
  public Map<String, Object> saveTaggingTransaction(TaggingTransactionsRequest request) {
    Map<String, Object> results = new HashMap<>();
    Optional<WeighbridgeStations> optionalWBSByCode =
        weighbridgeStationsServiceInterface.findByWBSId(request.getStationCode());

    if (optionalWBSByCode.isPresent()) {
      try {
        TaggingTransactions transaction =
            new TaggingTransactions()
                .setCreatedOn(new Date())
                .setTransactionDate(request.getTransactionTime())
                .setChargedReason(request.getChargedReason())
                .setConfirmedVehicle_no(request.getConfirmedVehicleNo())
                .setVehicleNo(request.getVehicleNo())
                .setEvidenceReference(request.getPhotoEvidence())
                .setEvidenceId(request.getEvidenceId())
                .setTaggingScene(request.getSceneOfTagging())
                .setTaggingSystem(request.getTaggingSystemUsed())
                .setTagOnChargeAmount(
                    new BigDecimal(
                        request.getChargedAmount() == null ? 0 : request.getChargedAmount()))
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
        // messagingTemplate.convertAndSend("/topic/tags-transactions", new
        // ObjectMapper().writeValueAsString(weighbridgeStations));
      } catch (Exception e) {
        System.out.println("e.getMessage() = " + e.getMessage());
      }
    } else {
    }

    return results;
  }

  @Override
  public Map<String, Object> saveHSWIMTransaction(HSWIMTransactionRequest request) {
    Map<String, Object> results = new HashMap<>();

    try {
      HSWIMTransaction transaction =
          new HSWIMTransaction()
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
