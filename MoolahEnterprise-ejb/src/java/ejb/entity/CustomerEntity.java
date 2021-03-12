/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entity;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import util.enumeration.GenderEnum;

/**
 *
 * @author nickg
 */
@Entity
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
    @Size(min = 6, max = 50)
    private String password;

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

    //bidirectional
    @OneToMany(mappedBy = "customer")
    private List<IssueEntity> listOfIssues;

    //unidirectional
    @OneToMany
    private List<ComparisonEntity> savedComparisons;

    //bidirectional
    @OneToOne(optional = false, cascade = {CascadeType.REMOVE})
    private AssetEntity asset;

    //unidirectional    
    @OneToMany
    private List<QuestionnaireEntity> listOfQuestionnaires;

    //unidirectional
    @OneToMany
    private List<ProductEntity> listOfLikeProducts;

    public CustomerEntity() {
        this.listOfIssues = new ArrayList<>();
        this.savedComparisons = new ArrayList<>();
        this.listOfLikeProducts = new ArrayList<>();
    }

    public CustomerEntity(String fullName, String email, String password, GregorianCalendar dateOfBirth, String phoneNumber, GenderEnum gender, Boolean smoker, AssetEntity asset) {
        this();
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.smoker = smoker;
        this.asset = asset;

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
        this.password = password;
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

    public List<QuestionnaireEntity> getListOfQuestionnaires() {
        return listOfQuestionnaires;
    }

    public void setListOfQuestionnaires(List<QuestionnaireEntity> listOfQuestionnaires) {
        this.listOfQuestionnaires = listOfQuestionnaires;
    }

    public List<ProductEntity> getListOfLikeProducts() {
        return listOfLikeProducts;
    }

    public void setListOfLikeProducts(List<ProductEntity> listOfLikeProducts) {
        this.listOfLikeProducts = listOfLikeProducts;
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
