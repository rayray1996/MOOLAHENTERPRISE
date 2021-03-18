/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

/**
 *
 * @author nickg
 */
@Entity
public class ComparisonEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comparisonId;
    
    @NotNull
    @Size(min=1)
    private String comparisonName;
    
    @NotNull
    @Temporal(TemporalType.DATE)
    @PastOrPresent
    private Calendar dateOfCompletion;
    
    //unidirectional
    @OneToMany
    private List<ProductEntity> productsToCompare;

    public ComparisonEntity() {
        productsToCompare = new ArrayList<>();
    }

    public ComparisonEntity(String comparisonName, Calendar dateOfCompletion, List<ProductEntity> productsToCompare) {
        this.comparisonName = comparisonName;
        this.dateOfCompletion = dateOfCompletion;
        this.productsToCompare = productsToCompare;
    }
    

    public Long getComparisonId() {
        return comparisonId;
    }

    public void setComparisonId(Long comparisonId) {
        this.comparisonId = comparisonId;
    }

    public String getComparisonName() {
        return comparisonName;
    }

    public void setComparisonName(String comparisonName) {
        this.comparisonName = comparisonName;
    }

    public Calendar getDateOfCompletion() {
        return dateOfCompletion;
    }

    public void setDateOfCompletion(Calendar dateOfCompletion) {
        this.dateOfCompletion = dateOfCompletion;
    }

    public List<ProductEntity> getProductsToCompare() {
        return productsToCompare;
    }

    public void setProductsToCompare(List<ProductEntity> productsToCompare) {
        this.productsToCompare = productsToCompare;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (comparisonId != null ? comparisonId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the comparisonId fields are not set
        if (!(object instanceof ComparisonEntity)) {
            return false;
        }
        ComparisonEntity other = (ComparisonEntity) object;
        if ((this.comparisonId == null && other.comparisonId != null) || (this.comparisonId != null && !this.comparisonId.equals(other.comparisonId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.entity.ComparisonEntity[ id=" + comparisonId + " ]";
    }
    
}
