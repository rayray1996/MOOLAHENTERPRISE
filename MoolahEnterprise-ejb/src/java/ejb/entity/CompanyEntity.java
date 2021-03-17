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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.security.CryptographicHelper;

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

    @Lob
    @Column(name = "company_profile")
    private byte[] profilePic;

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
    @Size(min = 1)
    private String warningMessage;

    @NotNull
    private Boolean isVerified;

    @NotNull
    @Size(min = 8, max = 20)
    private String password;

    @Temporal(TemporalType.DATE)
    private GregorianCalendar verificationDate;

    @NotNull
    private BigInteger creditOwned;

    @NotNull
    private Boolean isDeactivated;

    @NotNull
    private Boolean isDeleted;

    @NotNull
    private Boolean isWarned;

    @Column(columnDefinition = "CHAR(32) NOT NULL")
    private String salt;

    @OneToOne(mappedBy = "company", cascade = {CascadeType.MERGE})
    private RefundEntity refund;

    @OneToMany(mappedBy = "company", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(nullable = false)
    private List<PointOfContactEntity> listOfPointOfContacts;

    @OneToMany(mappedBy = "company", cascade = {CascadeType.MERGE})
    @JoinColumn(nullable = true)
    private List<MonthlyPaymentEntity> listOfMonthlyPayments;

    @OneToMany(mappedBy = "company", cascade = {CascadeType.MERGE})
    private List<ProductEntity> listOfProducts;

    public CompanyEntity() {
        this.refund = null;
        this.listOfPointOfContacts = new ArrayList<>();
        this.listOfMonthlyPayments = new ArrayList<>();
        this.listOfProducts = new ArrayList<>();
        this.isVerified = false;
        this.isDeleted = false;
        this.isDeactivated = false;
        this.verificationDate = null;
        this.warningMessage = "";
        this.salt = CryptographicHelper.getInstance().generateRandomString(32);
        this.profilePic = null;
        this.isWarned = false;
    }

    public CompanyEntity(String companyName, String companyEmail, String businessRegNumber ,String companyContactNumber, String password, BigInteger creditOwned) {
        this();
        this.companyName = companyName;
        this.companyEmail = companyEmail;
        this.companyContactNumber = companyContactNumber;
        this.password = password;
        this.creditOwned = creditOwned;
        this.businessRegNumber = businessRegNumber;

        setPassword(password);
    }

    public byte[] getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }

    public void setPassword(String password) {
        if (password != null) {
            this.password = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + this.salt));
        } else {
            this.password = null;
        }
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

    public Boolean isIsVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public String getPassword() {
        return password;
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

    public Boolean isIsDeactivated() {
        return isDeactivated;
    }

    public void setIsDeactivated(boolean isDeactivated) {
        this.isDeactivated = isDeactivated;
    }

    public Boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Boolean getIsWarned() {
        return isWarned;
    }

    public void setIsWarned(Boolean isWarned) {
        this.isWarned = isWarned;
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

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
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
