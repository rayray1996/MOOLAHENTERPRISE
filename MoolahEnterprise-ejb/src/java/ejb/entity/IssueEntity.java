/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import util.enumeration.ReasonForContactEnum;

/**
 *
 * @author nickg
 */
@Entity
public class IssueEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long issueId;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private ReasonForContactEnum reasonForContact;
    
    @NotNull
    private String issueDescription;

    public IssueEntity() {
    }

    public IssueEntity(ReasonForContactEnum reasonForContact, String issueDescription) {
        this.reasonForContact = reasonForContact;
        this.issueDescription = issueDescription;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public ReasonForContactEnum getReasonForContact() {
        return reasonForContact;
    }

    public void setReasonForContact(ReasonForContactEnum reasonForContact) {
        this.reasonForContact = reasonForContact;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (issueId != null ? issueId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the issueId fields are not set
        if (!(object instanceof IssueEntity)) {
            return false;
        }
        IssueEntity other = (IssueEntity) object;
        if ((this.issueId == null && other.issueId != null) || (this.issueId != null && !this.issueId.equals(other.issueId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.entity.IssueEntity[ id=" + issueId + " ]";
    }
    
}
