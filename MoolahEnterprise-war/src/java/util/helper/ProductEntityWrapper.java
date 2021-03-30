/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.helper;

import ejb.entity.ProductEntity;
import java.io.Serializable;

/**
 *
 * @author rayta
 */
public class ProductEntityWrapper implements Serializable{

    private ProductEntity productEntity;
    private String stringParentCategory;
    private String stringChildCategory;
    
    public ProductEntityWrapper() {
    }

    public ProductEntityWrapper(ProductEntity productEntity, String stringParentCategory, String stringChildCategory) {
        this.productEntity = productEntity;
        this.stringParentCategory = stringParentCategory;
        this.stringChildCategory = stringChildCategory;
    }

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public void setProductEntity(ProductEntity productEntity) {
        this.productEntity = productEntity;
    }

    public String getStringParentCategory() {
        return stringParentCategory;
    }

    public void setStringParentCategory(String stringParentCategory) {
        this.stringParentCategory = stringParentCategory;
    }

    public String getStringChildCategory() {
        return stringChildCategory;
    }

    public void setStringChildCategory(String stringChildCategory) {
        this.stringChildCategory = stringChildCategory;
    }
    
    
}
