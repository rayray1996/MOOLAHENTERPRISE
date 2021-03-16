/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.EndowmentEntity;
import ejb.entity.InvestmentLinkedEntity;
import ejb.entity.ProductEntity;
import ejb.entity.TermLifeProductEntity;
import ejb.entity.WholeLifeProductEntity;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;
import util.enumeration.CategoryEnum;
import util.exception.InvalidFilterCriteriaException;
import util.exception.ProductNotFoundException;

/**
 *
 * @author rayta
 */
@Local
public interface ProductSessionBeanLocal {

    public ProductEntity retrieveProductEntityById(Long productId) throws ProductNotFoundException;

    public List<WholeLifeProductEntity> retrieveAllWholeLifeProducts();

    public List<TermLifeProductEntity> retrieveAllTermLifeProducts();

    public List<InvestmentLinkedEntity> retrieveAllInvestmentLinkedProducts();

    public List<EndowmentEntity> retrieveAllEndowmentProducts();

    public List<ProductEntity> retrieveAllFinancialProducts();

    public List<ProductEntity> searchForProductsByName(String name);

    public List<ProductEntity> filterProductsByCriteria(CategoryEnum category, boolean wantsRider, boolean isSmoker, BigDecimal sumAssured, Integer coverageTerm, Integer premiumTerm) throws InvalidFilterCriteriaException;
    
}
