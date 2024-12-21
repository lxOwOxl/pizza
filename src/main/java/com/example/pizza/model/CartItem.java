package com.example.pizza.model;

import java.math.BigDecimal;
import java.util.List;

import com.example.pizza.entity.Combo;
import com.example.pizza.entity.CrustPrice;
import com.example.pizza.entity.Product;
import com.example.pizza.enums.Crust;
import com.example.pizza.enums.Size;

public class CartItem {
    private ProductDTO productDTO;
    private ComboDTO comboDTO;
    private Integer quantity;
    private BigDecimal totalPrice;

    public CartItem(ProductDTO productDTO, Integer quantity) {
        this.productDTO = productDTO;
        this.quantity = quantity;
    }

    public CartItem(ComboDTO comboDTO, Integer quantity) {
        this.comboDTO = comboDTO;
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ProductDTO getProductDTO() {
        return productDTO;
    }

    public void setProductDTO(ProductDTO productDTO) {
        this.productDTO = productDTO;
    }

    public ComboDTO getComboDTO() {
        return comboDTO;
    }

    public void setComboDTO(ComboDTO comboDTO) {
        this.comboDTO = comboDTO;
    }

}