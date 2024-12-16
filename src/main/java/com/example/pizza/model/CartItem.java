package com.example.pizza.model;

import java.math.BigDecimal;
import java.util.List;

import com.example.pizza.enums.Crust;
import com.example.pizza.enums.ProductType;
import com.example.pizza.enums.Size;

public class CartItem {
    private int id;
    private String name;
    private String image;
    private Size size;
    private Crust crust;
    private ProductType type;
    private Integer quantity;
    private BigDecimal totalPrice;
    private List<CartItem> productList;

    public CartItem(int id, String name, String image, Size size, Crust crust, ProductType type, Integer quantity) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.size = size;
        this.crust = crust;
        this.type = type;
        this.quantity = quantity;
    }

    public CartItem(int id, String name, String image, ProductType type, Integer quantity) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.type = type;
        this.quantity = quantity;
    }

    public CartItem(int id, String name, String image, Size size, Crust crust, ProductType type, Integer quantity,
            BigDecimal totalPrice, List<CartItem> productList) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.size = size;
        this.crust = crust;
        this.type = type;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.productList = productList;
    }

    public CartItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Crust getCrust() {
        return crust;
    }

    public void setCrust(Crust crust) {
        this.crust = crust;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
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

    public List<CartItem> getProductList() {
        return productList;
    }

    public void setProductList(List<CartItem> productList) {
        this.productList = productList;
    }

}
