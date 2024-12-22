package com.example.pizza.model;

import java.math.BigDecimal;

import com.example.pizza.entity.Product;
import com.example.pizza.enums.Crust;
import com.example.pizza.enums.Size;

public class PizzaVariant {
    private Crust crust;
    private Size size;
    private BigDecimal price;

    public PizzaVariant(Crust crust, Size size, BigDecimal price) {
        this.crust = crust;
        this.size = size;
        this.price = price;
    }

    // Getter and Setter methods
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
