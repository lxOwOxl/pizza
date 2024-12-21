package com.example.pizza.entity;

import java.math.BigDecimal;

import com.example.pizza.enums.Category;
import com.example.pizza.enums.Crust;
import com.example.pizza.enums.Size;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Combo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String image;
    private String description;
    @Column(precision = 10, scale = 2)
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private Category pizzaCate;

    @Enumerated(EnumType.STRING)
    private Category drinkCate;

    @Enumerated(EnumType.STRING)
    private Category sideDishCate;
    private int pizzaQuantity;
    private int drinkQuantity;
    private int sideDishQuantity;
    private Size size;
    private Crust crust;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Category getPizzaCate() {
        return pizzaCate;
    }

    public void setPizzaCate(Category pizzaCate) {
        this.pizzaCate = pizzaCate;
    }

    public Category getDrinkCate() {
        return drinkCate;
    }

    public void setDrinkCate(Category drinkCate) {
        this.drinkCate = drinkCate;
    }

    public Category getSideDishCate() {
        return sideDishCate;
    }

    public void setSideDishCate(Category sideDishCate) {
        this.sideDishCate = sideDishCate;
    }

    public int getPizzaQuantity() {
        return pizzaQuantity;
    }

    public void setPizzaQuantity(int pizzaQuantity) {
        this.pizzaQuantity = pizzaQuantity;
    }

    public int getDrinkQuantity() {
        return drinkQuantity;
    }

    public void setDrinkQuantity(int drinkQuantity) {
        this.drinkQuantity = drinkQuantity;
    }

    public int getSideDishQuantity() {
        return sideDishQuantity;
    }

    public void setSideDishQuantity(int sideDishQuantity) {
        this.sideDishQuantity = sideDishQuantity;
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

}
