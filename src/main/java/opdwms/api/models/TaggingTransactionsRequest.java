package opdwms.api.models;

import java.util.Date;

public class TaggingTransactionsRequest {
    private String tagReference;
    private String vehicleNo;
    private String transgression;
    private Date transactionTime;
    private String weighingReference;
    private String taggingSystemUsed;
    private String sceneOfTagging;
    private Long tagFlag;
    private String confirmedVehicleNo;
    private Long chargedAmount;
    private String  tagType;
    private String weighbridge;
    private String chargedReason;
    private String photoEvidence;
    private String evidenceId;
    private String stationCode;

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getTagReference() {
        return tagReference;
    }

    public TaggingTransactionsRequest setTagReference(String tagReference) {
        this.tagReference = tagReference;
        return this;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public TaggingTransactionsRequest setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
        return this;
    }

    public String getTransgression() {
        return transgression;
    }

    public TaggingTransactionsRequest setTransgression(String transgression) {
        this.transgression = transgression;
        return this;
    }

    public Date getTransactionTime() {
        return transactionTime;
    }

    public TaggingTransactionsRequest setTransactionTime(Date transactionTime) {
        this.transactionTime = transactionTime;
        return this;
    }

    public String getWeighingReference() {
        return weighingReference;
    }

    public TaggingTransactionsRequest setWeighingReference(String weighingReference) {
        this.weighingReference = weighingReference;
        return this;
    }

    public String getTaggingSystemUsed() {
        return taggingSystemUsed;
    }

    public TaggingTransactionsRequest setTaggingSystemUsed(String taggingSystemUsed) {
        this.taggingSystemUsed = taggingSystemUsed;
        return this;
    }

    public String getSceneOfTagging() {
        return sceneOfTagging;
    }

    public TaggingTransactionsRequest setSceneOfTagging(String sceneOfTagging) {
        this.sceneOfTagging = sceneOfTagging;
        return this;
    }

    public Long getTagFlag() {
        return tagFlag;
    }

    public TaggingTransactionsRequest setTagFlag(Long tagFlag) {
        this.tagFlag = tagFlag;
        return this;
    }

    public String getConfirmedVehicleNo() {
        return confirmedVehicleNo;
    }

    public TaggingTransactionsRequest setConfirmedVehicleNo(String confirmedVehicleNo) {
        this.confirmedVehicleNo = confirmedVehicleNo;
        return this;
    }

    public Long getChargedAmount() {
        return chargedAmount;
    }

    public TaggingTransactionsRequest setChargedAmount(Long chargedAmount) {
        this.chargedAmount = chargedAmount;
        return this;
    }

    public String getTagType() {
        return tagType;
    }

    public TaggingTransactionsRequest setTagType(String tagType) {
        this.tagType = tagType;
        return this;
    }

    public String getWeighbridge() {
        return weighbridge;
    }

    public TaggingTransactionsRequest setWeighbridge(String weighbridge) {
        this.weighbridge = weighbridge;
        return this;
    }

    public String getChargedReason() {
        return chargedReason;
    }

    public TaggingTransactionsRequest setChargedReason(String chargedReason) {
        this.chargedReason = chargedReason;
        return this;
    }

    public String getPhotoEvidence() {
        return photoEvidence;
    }

    public TaggingTransactionsRequest setPhotoEvidence(String photoEvidence) {
        this.photoEvidence = photoEvidence;
        return this;
    }

    public String getEvidenceId() {
        return evidenceId;
    }

    public TaggingTransactionsRequest setEvidenceId(String evidenceId) {
        this.evidenceId = evidenceId;
        return this;
    }

    @Override
    public String toString() {
        return String.format("TaggingTransactionsRequest " +
                        "[tagReference=%s, vehicleNo=%s, transgression=%s, transactionTime=%s, weighingReference=%s, taggingSystemUsed=%s, sceneOfTagging=%s," +
                        "tagFlag=%s, confirmedVehicleNo=%s, chargedAmount=%s, tagType=%s, weighbridge=%s, chargedReason=%s, tagEvidence=%s, tagId=%s]",
                getTagReference(), getVehicleNo(), getTransgression(), getTransactionTime(), getWeighingReference(), getTaggingSystemUsed(), getSceneOfTagging(),
                getTagFlag(), getConfirmedVehicleNo(), getChargedAmount(), getTagType(), getWeighbridge(), getChargedReason(), getPhotoEvidence(), getEvidenceId());
    }
}
