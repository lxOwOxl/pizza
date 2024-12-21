package com.example.pizza.model;

import java.util.List;

public class CartItem {
    private Integer productId;
    private Integer comboId;
    private Integer crustId;
    private Integer quantity;
    private List<Integer> productIds;

    public CartItem(Integer productId, Integer comboId, Integer crustId, Integer quantity, List<Integer> productIds) {
        this.productId = productId;
        this.comboId = comboId;
        this.crustId = crustId;
        this.quantity = quantity;
        this.productIds = productIds;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getComboId() {
        return comboId;
    }

    public void setComboId(Integer comboId) {
        this.comboId = comboId;
    }

    public Integer getCrustId() {
        return crustId;
    }

    public void setCrustId(Integer crustId) {
        this.crustId = crustId;
    }

    public List<Integer> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Integer> productIds) {
        this.productIds = productIds;
    }

}