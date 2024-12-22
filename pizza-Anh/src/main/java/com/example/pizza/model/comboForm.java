package com.example.pizza.model;

import java.util.ArrayList;
import java.util.List;

import com.example.pizza.entity.Product;

public class comboForm {
    private List<Product> products = new ArrayList<Product>();

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
