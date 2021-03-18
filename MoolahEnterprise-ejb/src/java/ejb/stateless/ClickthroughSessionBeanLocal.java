/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import java.math.BigInteger;
import javax.ejb.Local;
import util.exception.ProductNotFoundException;

/**
 *
 * @author rayta
 */
@Local
public interface ClickthroughSessionBeanLocal {

    public void addClickToProduct(Long productId) throws ProductNotFoundException;

    public BigInteger calculateMonthlyProductPrice(Long productId) throws ProductNotFoundException;
    
}
