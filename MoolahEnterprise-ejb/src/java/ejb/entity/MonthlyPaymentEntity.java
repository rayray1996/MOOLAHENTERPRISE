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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author rayta
 */
@Entity
public class MonthlyPaymentEntity extends PaymentEntity implements Serializable {

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<ProductLineItemEntity> listOfProductLineItems;

    @NotNull
    @Min(value = 1)
    private BigInteger totalPayable;

    public MonthlyPaymentEntity() {
        listOfProductLineItems = new ArrayList<>();
    }

    public MonthlyPaymentEntity(List<ProductLineItemEntity> listOfProductLineItems, Boolean paid, Calendar dateTransacted, String paymentNumber, BigInteger totalPayable, Calendar dateGenerated, CompanyEntity company) {
        super(paid, dateTransacted, paymentNumber, dateGenerated, company);
        this.listOfProductLineItems = listOfProductLineItems;
        this.totalPayable = totalPayable;
    }

    public List<ProductLineItemEntity> getListOfProductLineItems() {
        return listOfProductLineItems;
    }

    public void setListOfProductLineItems(List<ProductLineItemEntity> listOfProductLineItems) {
        this.listOfProductLineItems = listOfProductLineItems;
    }

    public BigInteger getTotalPayable() {
        return totalPayable;
    }

    public void setTotalPayable(BigInteger totalPayable) {
        this.totalPayable = totalPayable;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final MonthlyPaymentEntity other = (MonthlyPaymentEntity) obj;
        if (!Objects.equals(this.listOfProductLineItems, other.listOfProductLineItems)) {
            return false;
        }
        if (!Objects.equals(this.totalPayable, other.totalPayable)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MonthlyPaymentEntity{" + "listOfProductLineItems=" + listOfProductLineItems + ", totalPayable=" + totalPayable + '}';
    }
    
    

}
