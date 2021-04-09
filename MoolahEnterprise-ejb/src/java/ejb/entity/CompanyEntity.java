/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
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
    private String warningMessage;

    @NotNull
    private Boolean isVerified;

    @NotNull
//    @Size(min = 8, max = 100)
    private String password;

    @Temporal(TemporalType.DATE)
    private Calendar verificationDate;

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

    @OneToOne(mappedBy = "company", cascade = {CascadeType.MERGE, CascadeType.DETACH})
    private RefundEntity refund;

    @OneToMany(mappedBy = "company", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(nullable = false)
    private List<PointOfContactEntity> listOfPointOfContacts;

    @OneToMany(mappedBy = "company", cascade = {CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(nullable = true)
    private List<PaymentEntity> listOfPayments;

    @OneToMany(mappedBy = "company", cascade = {CascadeType.MERGE, CascadeType.DETACH})
    private List<ProductEntity> listOfProducts;

    private String resetPasswordPathParam;

    @NotNull
    private String companyImage;

    @NotNull
    private String companyUrl;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar expiryDateOfPathParam;

    public CompanyEntity() {
        this.refund = null;
        this.listOfPointOfContacts = new ArrayList<>();
        this.listOfPayments = new ArrayList<>();
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

    public CompanyEntity(String companyName, String companyEmail, String businessRegNumber, String companyContactNumber, String password, BigInteger creditOwned, String companyImage, String companyUrl) {
        this();
        this.companyName = companyName;
        this.companyEmail = companyEmail;
        this.companyContactNumber = companyContactNumber;
        this.password = password;
        this.creditOwned = creditOwned;
        this.businessRegNumber = businessRegNumber;
        this.companyImage = companyImage;
        this.companyUrl = companyUrl;

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

    public String getPassword() {
        return this.password;
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

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public Calendar getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(Calendar verificationDate) {
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

    public void setIsDeactivated(Boolean isDeactivated) {
        this.isDeactivated = isDeactivated;
    }

    public Boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
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

    public List<PaymentEntity> getListOfPayments() {
        return listOfPayments;
    }

    public void setListOfPayments(List<PaymentEntity> listOfPayments) {
        this.listOfPayments = listOfPayments;
    }

    public List<ProductEntity> getListOfProducts() {
        return listOfProducts;
    }

    public void setListOfProducts(List<ProductEntity> listOfProducts) {
        this.listOfProducts = listOfProducts;
    }

    public String getResetPasswordPathParam() {
        return resetPasswordPathParam;
    }

    public void setResetPasswordPathParam(String resetPasswordPathParam) {
        this.resetPasswordPathParam = resetPasswordPathParam;
    }

    public Calendar getExpiryDateOfPathParam() {
        return expiryDateOfPathParam;
    }

    public void setExpiryDateOfPathParam(Calendar expiryDateOfPathParam) {
        this.expiryDateOfPathParam = expiryDateOfPathParam;
    }

    public String getCompanyImage() {
        return companyImage;
    }

    public void setCompanyImage(String companyImage) {
        this.companyImage = companyImage;
    }

    public String getCompanyUrl() {
        return companyUrl;
    }

    public void setCompanyUrl(String companyUrl) {
        this.companyUrl = companyUrl;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final CompanyEntity other = (CompanyEntity) obj;
        if (!Objects.equals(this.companyId, other.companyId)) {
            return false;
        }
        return true;
    }


}
