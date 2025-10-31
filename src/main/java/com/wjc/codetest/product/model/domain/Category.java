package com.wjc.codetest.product.model.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Category {

    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

    protected Category() {

    }

    public Category(String name) {
        this.name = name;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Long productId) {
        products.removeIf(product -> product.getId().equals(productId));
    }
}
