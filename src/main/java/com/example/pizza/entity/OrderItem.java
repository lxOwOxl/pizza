package com.example.pizza.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.example.pizza.enums.Crust;
import com.example.pizza.enums.Size;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "combo_id")
    private Combo combo;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "crust_id")
    private CrustPrice crustPrice;

    @OneToMany(mappedBy = "orderItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComboItem> comboItems = new ArrayList<>();

    private BigDecimal price;

    public OrderItem() {
    }

    public OrderItem(Order order, Product product, Combo combo, int quantity, CrustPrice crustPrice, BigDecimal price,
            List<ComboItem> comboItems) {
        this.order = order;
        this.comboItems = comboItems;
        this.product = product;
        this.combo = combo;
        this.quantity = quantity;
        this.crustPrice = crustPrice;
        this.price = price;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Product getProductt() {
        return product;
    }

    public void setProductt(Product product) {
        this.product = product;
    }

    public Combo getCombo() {
        return combo;
    }

    public void setCombo(Combo combo) {
        this.combo = combo;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public CrustPrice getCrustPrice() {
        return crustPrice;
    }

    public void setCrustPrice(CrustPrice crustPrice) {
        this.crustPrice = crustPrice;
    }

}
