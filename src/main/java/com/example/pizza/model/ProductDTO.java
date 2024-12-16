package com.example.pizza.model;

import com.example.pizza.enums.Crust;
import com.example.pizza.enums.ProductType;
import com.example.pizza.enums.Size;

public class ProductDTO {
    private Integer id;
    private String name;
    private String image;
    private ProductType type;
    private Crust crust;
    private Size size;

    public ProductDTO(Integer id, String name, String image, ProductType type, Crust crust, Size size) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.type = type;
        this.crust = crust;
        this.size = size;
    }

    public ProductDTO(Integer id, String name, String image, ProductType type) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.type = type;
    }

    public boolean equals(ProductDTO productDTO) {
        return productDTO.id == this.id && productDTO.crust == this.crust && productDTO.size == this.size;
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

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public Crust getCrust() {
        return crust;
    }

    public void setCrust(Crust crust) {
        this.crust = crust;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

}
