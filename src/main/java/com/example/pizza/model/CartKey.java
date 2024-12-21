package com.example.pizza.model;

import com.example.pizza.enums.Crust;
import com.example.pizza.enums.ProductType;
import com.example.pizza.enums.Size;

import java.util.Objects;

public class CartKey {
    private int id;
    private ProductType type;
    private Crust crust;
    private Size size;

    public CartKey(int id, ProductType type, Crust crust, Size size) {
        this.id = id;
        this.type = type;
        this.crust = crust;
        this.size = size;
    }

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CartKey cartKey = (CartKey) o;
        return id == cartKey.id &&
                type == cartKey.type &&
                crust == cartKey.crust &&
                size == cartKey.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, crust, size);
    }
}
