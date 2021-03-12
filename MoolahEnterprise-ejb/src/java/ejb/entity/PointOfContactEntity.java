/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author rayta
 */
@Entity
public class PointOfContactEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pocId;

    @NotNull
    @Size(min = 2)
    private String pocName;

    @NotNull
    @Size(min = 8)
    private String pocMobileNumber;

    @NotNull
    @Size(min = 8)
    private String pocOfficeNumber;

    @NotNull
    @Email
    private String pocEmail;
    
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(nullable = false)
    private CompanyEntity company;

    public PointOfContactEntity() {
    }

    public PointOfContactEntity(String pocName, String pocMobileNumber, String pocOfficeNumber, String pocEmail, CompanyEntity company) {
        this.pocName = pocName;
        this.pocMobileNumber = pocMobileNumber;
        this.pocOfficeNumber = pocOfficeNumber;
        this.pocEmail = pocEmail;
        this.company = company;
    }

    public Long getPocId() {
        return pocId;
    }

    public void setPocId(Long pocId) {
        this.pocId = pocId;
    }

    public String getPocName() {
        return pocName;
    }

    public void setPocName(String pocName) {
        this.pocName = pocName;
    }

    public String getPocMobileNumber() {
        return pocMobileNumber;
    }

    public void setPocMobileNumber(String pocMobileNumber) {
        this.pocMobileNumber = pocMobileNumber;
    }

    public String getPocOfficeNumber() {
        return pocOfficeNumber;
    }

    public void setPocOfficeNumber(String pocOfficeNumber) {
        this.pocOfficeNumber = pocOfficeNumber;
    }

    public String getPocEmail() {
        return pocEmail;
    }

    public void setPocEmail(String pocEmail) {
        this.pocEmail = pocEmail;
    }

    public CompanyEntity getCompany() {
        return company;
    }

    public void setCompany(CompanyEntity company) {
        this.company = company;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PointOfContactEntity other = (PointOfContactEntity) obj;
        if (!Objects.equals(this.pocName, other.pocName)) {
            return false;
        }
        if (!Objects.equals(this.pocMobileNumber, other.pocMobileNumber)) {
            return false;
        }
        if (!Objects.equals(this.pocOfficeNumber, other.pocOfficeNumber)) {
            return false;
        }
        if (!Objects.equals(this.pocEmail, other.pocEmail)) {
            return false;
        }
        if (!Objects.equals(this.pocId, other.pocId)) {
            return false;
        }
        if (!Objects.equals(this.company, other.company)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PointOfContactEntity{" + "pocId=" + pocId + ", pocName=" + pocName + ", pocMobileNumber=" + pocMobileNumber + ", pocOfficeNumber=" + pocOfficeNumber + ", pocEmail=" + pocEmail + ", company=" + company + '}';
    }
}
