package opdwms.web.weighingtransactions.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import opdwms.web.usermanager.entities.Users;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tag_clearances_report")
public class TaggingClearanceReport {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "tag_reference", length = 150)
    private String tagReference;

    @Column(name = "reference_no", length = 150)
    private String referenceNo;

    @Column(name = "tagging_transactions_no")
    private Long taggingTransactionsNo;

    @JoinColumn(name = "tagging_transactions_no", referencedColumnName = "ID", insertable = false, updatable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private TaggingTransactions taggingTransactionsLink;

    @Column(name = "narration", length = 150)
    private String narration;

    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Column(name = "CREATED_BY")
    private Long createdBy;

    //DISCLAIMER: RELATION NOT ENFORCED IN THE DB
    @JoinColumn(name = "CREATED_BY", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Users createdByLink;

    @JsonIgnore
    public TaggingTransactions getTaggingTransactionsLink() {
        return taggingTransactionsLink;
    }

    @JsonIgnore
    public Users getCreatedByLink() {
        return createdByLink;
    }

    public Long getId() {
        return id;
    }

    public TaggingClearanceReport setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTagReference() {
        return tagReference;
    }

    public TaggingClearanceReport setTagReference(String tagReference) {
        this.tagReference = tagReference;
        return this;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public TaggingClearanceReport setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
        return this;
    }

    public Long getTaggingTransactionsNo() {
        return taggingTransactionsNo;
    }

    public TaggingClearanceReport setTaggingTransactionsNo(Long taggingTransactionsNo) {
        this.taggingTransactionsNo = taggingTransactionsNo;
        return this;
    }

    public String getNarration() {
        return narration;
    }

    public TaggingClearanceReport setNarration(String narration) {
        this.narration = narration;
        return this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public TaggingClearanceReport setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public TaggingClearanceReport createdOn(Long userId) {
        this.createdOn = new Date(System.currentTimeMillis());
        this.createdBy = userId;
        return this;
    }
    public Long getCreatedBy() {
        return createdBy;
    }

    public TaggingClearanceReport setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TaggingClearanceReport)) {
            return false;
        }
        TaggingClearanceReport other = (TaggingClearanceReport) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
