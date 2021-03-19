/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.EndowmentEntity;
import ejb.entity.FeatureEntity;
import ejb.entity.PremiumEntity;
import ejb.entity.ProductEntity;
import ejb.entity.RiderEntity;
import ejb.entity.TermLifeProductEntity;
import ejb.entity.WholeLifeProductEntity;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;
import util.enumeration.CategoryEnum;
import util.enumeration.EndowmentProductEnum;
import util.enumeration.TermLifeProductEnum;
import util.enumeration.WholeLifeProductEnum;
import util.exception.CompanyDoesNotExistException;
import util.exception.InvalidFilterCriteriaException;
import util.exception.InvalidProductCreationException;
import util.exception.ProductAlreadyExistsException;
import util.exception.ProductNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author rayta
 */
@Local
public interface ProductSessionBeanLocal {

    public void deleteProductListing(Long productId) throws ProductNotFoundException;

    public void updateProductListing(ProductEntity updateProduct) throws ProductAlreadyExistsException, UnknownPersistenceException, InvalidProductCreationException;

    public List<ProductEntity> searchForProductsByName(String name);

    public ProductEntity retrieveProductEntityById(Long productId) throws ProductNotFoundException;

    public List<WholeLifeProductEntity> retrieveAllWholeLifeProducts();

    public List<TermLifeProductEntity> retrieveAllTermLifeProducts();

    public List<EndowmentEntity> retrieveAllEndowmentProducts();

    public List<ProductEntity> retrieveAllFinancialProducts();

    public List<ProductEntity> retrieveListOfProductByCompany(String email) throws CompanyDoesNotExistException;

    public List<ProductEntity> filterProductsByCriteria(CategoryEnum category, boolean wantsRider, boolean isSmoker, BigDecimal sumAssured, Integer coverageTerm, Integer premiumTerm, EndowmentProductEnum endowmentProductEnum, TermLifeProductEnum termLifeProductEnum, WholeLifeProductEnum wholeLifeProductEnum) throws InvalidFilterCriteriaException;

    public ProductEntity createProductListing(ProductEntity newProduct, Long companyId, List<RiderEntity> riders, List<PremiumEntity> premiums, List<FeatureEntity> features) throws ProductAlreadyExistsException, UnknownPersistenceException, InvalidProductCreationException;

   
}
