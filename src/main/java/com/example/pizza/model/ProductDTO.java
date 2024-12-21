package com.example.pizza.model;

import java.math.BigDecimal;

import com.example.pizza.entity.CrustPrice;
import com.example.pizza.entity.Product;
import com.example.pizza.enums.Crust;
import com.example.pizza.enums.ProductType;
import com.example.pizza.enums.Size;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProductDTO {
    private Integer id;
    private String name;
    private String image;
    private ProductType type;
    private Size size;
    private Crust crust;
    private BigDecimal price;

    public ProductDTO(Integer id, String name, String image, ProductType type, Size size, Crust crust,
            BigDecimal price) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.type = type;
        this.size = size;
        this.crust = crust;
        this.price = price;
    }

    public ProductDTO(Integer id, String name, ProductType type, Size size, Crust crust) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.size = size;
        this.crust = crust;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

}
