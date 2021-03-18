/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.AssetEntity;
import ejb.entity.CustomerEntity;
import ejb.entity.ProductEntity;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;
import util.exception.CustomerAlreadyExistException;
import util.exception.CustomerCreationException;
import util.exception.CustomerDoesNotExistsException;
import util.exception.CustomerPasswordExistsException;
import util.exception.CustomerUpdateException;
import util.exception.IncorrectLoginParticularsException;
import util.exception.ProductNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nickg
 */
@Local
public interface CustomerSessionBeanLocal {

    public CustomerEntity createCustomer(CustomerEntity newCust) throws CustomerAlreadyExistException, UnknownPersistenceException, CustomerCreationException;

    public CustomerEntity login(String email, String password) throws IncorrectLoginParticularsException;

    public void resetPassword(String email, String password) throws CustomerPasswordExistsException, CustomerDoesNotExistsException;

    public void updateCustomer(CustomerEntity newCust) throws CustomerDoesNotExistsException, UnknownPersistenceException, CustomerUpdateException;

    public CustomerEntity retrieveCustomerById(Long id) throws CustomerDoesNotExistsException;

    public void likeAProduct(Long custID, Long likedProdId) throws CustomerDoesNotExistsException, ProductNotFoundException;

    public void removeLikedProduct(Long custId, Long prodId) throws ProductNotFoundException, CustomerDoesNotExistsException;

    public List<ProductEntity> viewLikedProductList(Long custId) throws CustomerDoesNotExistsException;

    public AssetEntity retrieveMyAsset(Long custId) throws CustomerDoesNotExistsException;

    public Integer getAgeOfCustomer(CustomerEntity customer);

    public List<BigDecimal> getThreeYearsOfCapital(Long customerId) throws CustomerDoesNotExistsException;

    public boolean canAffordProduct(CustomerEntity customer, ProductEntity product, List<BigDecimal> nextThreeYearsOfCapital) throws ProductNotFoundException, CustomerDoesNotExistsException;

    public List<ProductEntity> retrieveRecommendedProducts(Long customerId) throws CustomerDoesNotExistsException, ProductNotFoundException;

}
