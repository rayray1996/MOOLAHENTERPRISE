/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author sohqi
 */
@Entity
public class FeatureEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long featureId;
    @Size(min=1)
    @NotNull
    private String featureDescription;

    public FeatureEntity() {
    }

    public FeatureEntity(String featureDescription) {
        this.featureDescription = featureDescription;
    }
    
    
    public String getFeatureDescription() {
        return featureDescription;
    }

    public void setFeatureDescription(String featureDescription) {
        this.featureDescription = featureDescription;
    }
    
    public Long getFeatureId() {
        return featureId;
    }
    
    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (featureId != null ? featureId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the featureId fields are not set
        if (!(object instanceof FeatureEntity)) {
            return false;
        }
        FeatureEntity other = (FeatureEntity) object;
        if ((this.featureId == null && other.featureId != null) || (this.featureId != null && !this.featureId.equals(other.featureId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.entity.FeatureEntity[ id=" + featureId + " ]";
    }
    
}
