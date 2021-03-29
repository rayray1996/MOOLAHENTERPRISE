/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entity;

import java.io.Serializable;
import java.util.Objects;
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
    
    @Size(min = 1)
    @NotNull
    private String featureName;
    
    @Size(min=1)
    @NotNull
    private String featureDescription;

    public FeatureEntity() {
    }

    public FeatureEntity(String featureName, String featureDescription) {
        this.featureName = featureName;
        this.featureDescription = featureDescription;
    }
    
    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
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
        final FeatureEntity other = (FeatureEntity) obj;
        if (!Objects.equals(this.featureId, other.featureId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FeatureEntity{" + "featureId=" + featureId + ", featureName=" + featureName + ", featureDescription=" + featureDescription + '}';
    }
    
   
    
}
