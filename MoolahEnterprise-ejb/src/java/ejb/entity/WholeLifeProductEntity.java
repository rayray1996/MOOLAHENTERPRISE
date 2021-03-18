/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import util.enumeration.WholeLifeProductEnum;

/**
 *
 * @author sohqi
 */
@Entity
public class WholeLifeProductEntity extends ProductEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Enumerated(EnumType.STRING)
    private WholeLifeProductEnum productEnum;

    public WholeLifeProductEntity() {
    }

    public WholeLifeProductEntity(WholeLifeProductEnum productEnum, String productName, Integer coverageTerm, BigDecimal assuredSum, String description, Boolean isDeleted, Integer premiumTerm) {
        super(productName, coverageTerm, assuredSum, description, isDeleted, premiumTerm);
        this.productEnum = productEnum;
    }

    public WholeLifeProductEnum getProductEnum() {
        return productEnum;
    }

    public void setProductEnum(WholeLifeProductEnum productEnum) {
        this.productEnum = productEnum;
    }

    @Override
    public String toString() {
        return "WholeLifeProductEntity{" + "productEnum=" + productEnum + '}';
    }

}
