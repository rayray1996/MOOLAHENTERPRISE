/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import util.enumeration.GenderEnum;
import util.security.CryptographicHelper;

/**
 *
 * @author nickg
 */
@Entity
@NamedQuery(name = "findCustWithEmail", query = "SELECT c FROM CustomerEntity c WHERE c.email =:custEmail")
public class CustomerEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @NotNull
    @Size(min = 3)
    private String fullName;

    @NotNull
    @Column(unique = true)
    @Email
    private String email;

    @NotNull
    @Column(unique = true)
//    @Size(min = 6, max = 100)
    private String password;

    @Column(columnDefinition = "CHAR(32) NOT NULL")
    private String salt;

    @NotNull
    @Past
    @Temporal(TemporalType.DATE)
    private GregorianCalendar dateOfBirth;

    @NotNull
    @Column(unique = true)
    @Size(min = 8, max = 15)
    private String phoneNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @NotNull
    private Boolean smoker;

    @NotNull
    private Boolean isMarried;

    //bidirectional
    @OneToMany(mappedBy = "customer")
    private List<IssueEntity> listOfIssues;

    //unidirectional
    @OneToMany
    private List<ComparisonEntity> savedComparisons;

    //Unidirectional
    @OneToOne(optional = false, cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true)
    private AssetEntity asset;

    //unidirectional
    @OneToMany
    private List<ProductEntity> listOfLikeProducts;

    private String resetPasswordPathParam;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar expiryDateOfPathParam;

    public CustomerEntity() {
        this.listOfIssues = new ArrayList<>();
        this.savedComparisons = new ArrayList<>();
        this.listOfLikeProducts = new ArrayList<>();
        this.salt = CryptographicHelper.getInstance().generateRandomString(32);
        this.resetPasswordPathParam = null;
        this.expiryDateOfPathParam = null;
        this.asset = new AssetEntity();
    }

    public CustomerEntity(String fullName, String email, String password, GregorianCalendar dateOfBirth, String phoneNumber, GenderEnum gender, Boolean smoker, AssetEntity asset, Boolean isMarried) {
        this();
        this.fullName = fullName;
        this.email = email;
        setPassword(password);
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.smoker = smoker;
        this.asset = asset;
        this.resetPasswordPathParam = null;
        this.expiryDateOfPathParam = null;
        this.isMarried = isMarried;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password != null) {
            this.password = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + this.getSalt()));
        } else {
            this.password = null;
        }
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public GregorianCalendar getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(GregorianCalendar dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public Boolean getSmoker() {
        return smoker;
    }

    public void setSmoker(Boolean smoker) {
        this.smoker = smoker;
    }

    public List<IssueEntity> getListOfIssues() {
        return listOfIssues;
    }

    public void setListOfIssues(List<IssueEntity> listOfIssues) {
        this.listOfIssues = listOfIssues;
    }

    public List<ComparisonEntity> getSavedComparisons() {
        return savedComparisons;
    }

    public void setSavedComparisons(List<ComparisonEntity> savedComparisons) {
        this.savedComparisons = savedComparisons;
    }

    public AssetEntity getAsset() {
        return asset;
    }

    public void setAsset(AssetEntity asset) {
        this.asset = asset;
    }

    public List<ProductEntity> getListOfLikeProducts() {
        return listOfLikeProducts;
    }

    public void setListOfLikeProducts(List<ProductEntity> listOfLikeProducts) {
        this.listOfLikeProducts = listOfLikeProducts;
    }

    public Boolean getIsMarried() {
        return isMarried;
    }

    public void setIsMarried(Boolean isMarried) {
        this.isMarried = isMarried;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customerId fields are not set
        if (!(object instanceof CustomerEntity)) {
            return false;
        }
        CustomerEntity other = (CustomerEntity) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.entity.CustomerEntity[ id=" + customerId + " ]";
    }

}
