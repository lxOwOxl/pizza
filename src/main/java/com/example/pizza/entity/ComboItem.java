package com.example.pizza.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "combo_items")
public class ComboItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderItem_id")
    private OrderItem orderItem; // Liên kết với bảng combo

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id") // Lưu chỉ ID của Product
    private Product product; // ID của sản phẩm

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crust_id")
    private CrustPrice crustPrice;

    // Getters và Setters

    public ComboItem(Product product, CrustPrice crustPrice) {
        this.product = product;
        this.crustPrice = crustPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }
}
