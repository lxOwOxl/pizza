package com.example.pizza.model;

import java.math.BigDecimal;
import java.util.List;

import com.example.pizza.enums.Crust;
import com.example.pizza.enums.Size;

public class ComboDTO {
    private Integer id;
    private String name;
    private String image;
    private BigDecimal price;
    private List<ProductDTO> productDTOs;

    public ComboDTO(Integer id, String name, String image, BigDecimal price, List<ProductDTO> productDTOs) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.productDTOs = productDTOs;
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

    public List<ProductDTO> getProductDTOs() {
        return productDTOs;
    }

    public void setProductDTOs(List<ProductDTO> productDTOs) {
        this.productDTOs = productDTOs;
    }

}
