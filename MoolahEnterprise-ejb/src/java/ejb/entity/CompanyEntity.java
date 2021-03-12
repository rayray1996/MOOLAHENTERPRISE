/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author rayta
 */
@Entity
public class CompanyEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;

    @NotNull
    @Size(min = 1)
    private String companyName;

    @NotNull
    @Email
    @Column(unique = true)
    private String companyEmail;

    @NotNull
    @Size(max = 20)
    private String businessRegNumber;

    @NotNull
    @Size(min = 8)
    private String companyContactNumber;

    @NotNull
    private boolean isVerified;

    @NotNull
    @Size(min = 8, max = 20)
    private String password;

    @Temporal(TemporalType.DATE)
    private GregorianCalendar verificationDate;

    @NotNull
    private BigInteger creditOwned;

    private boolean isDeactivated;

    @NotNull
    private boolean isDeleted;

    @NotNull
    private String salt;

    @OneToOne(mappedBy = "company")
    private RefundEntity refund;

    @OneToMany(mappedBy = "company")
    @Column(nullable = false)
    private List<PointOfContactEntity> listOfPointOfContacts;

    @OneToMany(mappedBy = "company")
    @Column(nullable = true)
    private List<MonthlyPaymentEntity> listOfMonthlyPayments;

    @OneToMany(mappedBy = "company")
    private List<ProductEntity> listOfProducts;

    public CompanyEntity() {
        refund = null;
        listOfPointOfContacts = new ArrayList<>();
        listOfMonthlyPayments = new ArrayList<>();
        listOfProducts = new ArrayList<>();
        isVerified = false;
        isDeleted = false;
        isDeactivated = false;
        verificationDate = null;
    }

    public CompanyEntity(String companyName, String companyEmail, String companyContactNumber, String password, BigInteger creditOwned) {
        this();
        this.companyName = companyName;
        this.companyEmail = companyEmail;
        this.companyContactNumber = companyContactNumber;
        this.password = password;
        this.creditOwned = creditOwned;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getBusinessRegNumber() {
        return businessRegNumber;
    }

    public void setBusinessRegNumber(String businessRegNumber) {
        this.businessRegNumber = businessRegNumber;
    }

    public String getCompanyContactNumber() {
        return companyContactNumber;
    }

    public void setCompanyContactNumber(String companyContactNumber) {
        this.companyContactNumber = companyContactNumber;
    }

    public boolean isIsVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public GregorianCalendar getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(GregorianCalendar verificationDate) {
        this.verificationDate = verificationDate;
    }

    public BigInteger getCreditOwned() {
        return creditOwned;
    }

    public void setCreditOwned(BigInteger creditOwned) {
        this.creditOwned = creditOwned;
    }

    public boolean isIsDeactivated() {
        return isDeactivated;
    }

    public void setIsDeactivated(boolean isDeactivated) {
        this.isDeactivated = isDeactivated;
    }

    public boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public RefundEntity getRefund() {
        return refund;
    }

    public void setRefund(RefundEntity refund) {
        this.refund = refund;
    }

    public List<PointOfContactEntity> getListOfPointOfContacts() {
        return listOfPointOfContacts;
    }

    public void setListOfPointOfContacts(List<PointOfContactEntity> listOfPointOfContacts) {
        this.listOfPointOfContacts = listOfPointOfContacts;
    }

    public List<MonthlyPaymentEntity> getListOfMonthlyPayments() {
        return listOfMonthlyPayments;
    }

    public void setListOfMonthlyPayments(List<MonthlyPaymentEntity> listOfMonthlyPayments) {
        this.listOfMonthlyPayments = listOfMonthlyPayments;
    }

    public List<ProductEntity> getListOfProducts() {
        return listOfProducts;
    }

    public void setListOfProducts(List<ProductEntity> listOfProducts) {
        this.listOfProducts = listOfProducts;
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
        final CompanyEntity other = (CompanyEntity) obj;
        if (!Objects.equals(this.companyId, other.companyId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CompanyEntity{" + "companyId=" + companyId + ", companyName=" + companyName + ", companyEmail=" + companyEmail + ", businessRegNumber=" + businessRegNumber + ", companyContactNumber=" + companyContactNumber + ", isVerified=" + isVerified + ", password=" + password + ", verificationDate=" + verificationDate + ", creditOwned=" + creditOwned + ", isDeactivated=" + isDeactivated + ", isDeleted=" + isDeleted + ", salt=" + salt + ", refund=" + refund + ", listOfPointOfContacts=" + listOfPointOfContacts + ", listOfMonthlyPayments=" + listOfMonthlyPayments + ", listOfProducts=" + listOfProducts + '}';
    }

    
}