/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Ada Wong
 */
@Entity
public class OptionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionId;
    @Column(unique = true)
    private String optionDescription; 
    private Boolean optionSelected;

    public OptionEntity() {
    }

    public OptionEntity(String optionDescription) {
        this.optionDescription = optionDescription;
        this.optionSelected = false;
    }

    public OptionEntity(String optionDescription, Boolean optionSelected) {
        this.optionDescription = optionDescription;
        this.optionSelected = optionSelected;
    }

    
    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (optionId != null ? optionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the optionId fields are not set
        if (!(object instanceof OptionEntity)) {
            return false;
        }
        OptionEntity other = (OptionEntity) object;
        if ((this.optionId == null && other.optionId != null) || (this.optionId != null && !this.optionId.equals(other.optionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.entity.OptionEntity[ id=" + optionId + " ]";
    }

    public String getOptionDescription() {
        return optionDescription;
    }

    public void setOptionDescription(String optionDescription) {
        this.optionDescription = optionDescription;
    }

    public Boolean getOptionSelected() {
        return optionSelected;
    }

    public void setOptionSelected(Boolean optionSelected) {
        this.optionSelected = optionSelected;
    }
    
}
