/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.Singleton;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.ejb.Singleton;

/**
 *
 * @author rayta
 */
@Singleton
public class MoolahCreditConverter implements MoolahCreditConverterLocal {

    private final BigDecimal costOfCreditInSGD = new BigDecimal(0.1);

    public MoolahCreditConverter() {
    }

    @Override
    public BigDecimal getCostOfCreditInSGD() {
        return costOfCreditInSGD;
    }
    
    @Override
    public BigDecimal convertCreditToSgd(BigInteger credit) {
        return costOfCreditInSGD.multiply(new BigDecimal(credit));
    }
    
    @Override
    public BigDecimal convertSgdToCredit(BigDecimal dollars) {
        return dollars.divide(costOfCreditInSGD);
    }
}
