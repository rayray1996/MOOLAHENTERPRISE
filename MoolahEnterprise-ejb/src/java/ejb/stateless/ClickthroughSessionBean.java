/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import ejb.entity.CategoryPricingEntity;
import ejb.entity.ProductEntity;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.ProductNotFoundException;

/**
 *
 * @author rayta
 */
@Stateless
public class ClickthroughSessionBean implements ClickthroughSessionBeanLocal {

    @PersistenceContext(unitName = "MoolahEnterprise-ejbPU")
    private EntityManager em;

    public ClickthroughSessionBean() {
    }

    @Override
    public void addClickToProduct(Long productId) throws ProductNotFoundException {
        ProductEntity product = em.find(ProductEntity.class, productId);
        if (product == null) {
            throw new ProductNotFoundException("Product is not found");
        } else {

            BigInteger newCounterMonth = product.getClickThroughInfo().getMonthCounter().add(BigInteger.ONE);
            product.getClickThroughInfo().setMonthCounter(newCounterMonth);
            System.out.println("Clickthrough month count: " + product.getClickThroughInfo().getMonthCounter());

            BigInteger newCounterYear = product.getClickThroughInfo().getOverallCounter().add(BigInteger.ONE);
            product.getClickThroughInfo().setOverallCounter(newCounterYear);
            System.out.println("Clickthrough overall count: " + product.getClickThroughInfo().getOverallCounter());

        }
    }

    @Override
    public BigInteger calculateMonthlyProductPrice(Long productId) throws ProductNotFoundException {
        ProductEntity product = em.find(ProductEntity.class, productId);
        if (product == null) {
            throw new ProductNotFoundException("Product is not found");
        } else {
            CategoryPricingEntity categoryPricing = product.getProductCategoryPricing();
            BigInteger creditPerClick = categoryPricing.getCreditPerClick();
            BigInteger monthlyCreditPrice = categoryPricing.getFixedSubscriptionCredit();
            BigInteger total = (product.getClickThroughInfo().getMonthCounter().multiply(creditPerClick)).add(monthlyCreditPrice);
            return total;
        }
    }
}
