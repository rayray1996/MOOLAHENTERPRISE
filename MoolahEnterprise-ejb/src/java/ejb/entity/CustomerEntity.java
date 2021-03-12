/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entity;

import java.io.Serializable;
import java.util.GregorianCalendar;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
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
    private String fullName;
    
    @NotNull
    private String email;
    
    @NotNull
    private String password;
    
    @NotNull
    private GregorianCalendar dateOfBirth;
    
    @NotNull
    private String phoneNumber;
    
    @NotNull
    @Enumerated
    private GenderEnum gender;
    
    @NotNull
    private Boolean smoker;

    @NotNull
    public CustomerEntity() {
    }

    public CustomerEntity(String fullName, String email, String password, GregorianCalendar dateOfBirth, String phoneNumber, GenderEnum gender, Boolean smoker) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.smoker = smoker;
    }
    

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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
