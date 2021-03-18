/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import util.enumeration.TermLifeProductEnum;

/**
 *
 * @author sohqi
 */
@Entity
public class TermLifeProductEntity extends ProductEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private TermLifeProductEnum productEnum;

    public TermLifeProductEntity() {
    }

    public TermLifeProductEntity(TermLifeProductEnum productEnum, String productName, Integer coverageTerm, BigDecimal assuredSum, String description, Boolean isDeleted, Integer premiumTerm) {
        super(productName, coverageTerm, assuredSum, description, isDeleted, premiumTerm);
        this.productEnum = productEnum;
    }

    public TermLifeProductEnum getProductEnum() {
        return productEnum;
    }

    public void setProductEnum(TermLifeProductEnum productEnum) {
        this.productEnum = productEnum;
    }

    @Override
    public String toString() {
        return "TermLifeProductEntity{" + "productEnum=" + productEnum + '}';
    }

    
}
