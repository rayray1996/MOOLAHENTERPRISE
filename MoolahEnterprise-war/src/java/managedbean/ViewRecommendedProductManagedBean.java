/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import ejb.entity.ProductEntity;
import ejb.stateless.ProductSessionBeanLocal;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.enumeration.CategoryEnum;
import util.enumeration.EndowmentProductEnum;
import util.enumeration.TermLifeProductEnum;
import util.enumeration.WholeLifeProductEnum;

/**
 *
 * @author rayta
 */
@Named(value = "viewRecommendedProductManagedBean")
@ViewScoped
public class ViewRecommendedProductManagedBean implements Serializable {

    @EJB
    private ProductSessionBeanLocal productSessionBean;

    private CustomerEntity customer;
    
    private List<ProductEntity> listOfProducts;
    private ProductEntity productToView;
    private List<CategoryEnum> categories;
    private CategoryEnum filterCategory;
    private Integer filterCoverageTerm;
    private Integer filterPremiumTerm;
    private BigDecimal filterSumAssured;

    public ViewRecommendedProductManagedBean() {
        
    }

    @PostConstruct
    public void dataInit() {
        listOfProducts = productSessionBean.filterProductsByCriteria(filterCategory, true, true, filterSumAssured, filterCoverageTerm, filterPremiumTerm, EndowmentProductEnum.ENDOWMENT, TermLifeProductEnum.ACCIDENT, WholeLifeProductEnum.LIFEINSURANCE)
    }
}
