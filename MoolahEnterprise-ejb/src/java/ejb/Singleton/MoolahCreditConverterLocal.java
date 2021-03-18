/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.Singleton;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.ejb.Local;

/**
 *
 * @author rayta
 */
@Local
public interface MoolahCreditConverterLocal {

    public BigDecimal getCostOfCreditInSGD();

    public BigDecimal convertCreditToSgd(BigInteger credit);

    public BigDecimal convertSgdToCredit(BigDecimal dollars);
    
}
