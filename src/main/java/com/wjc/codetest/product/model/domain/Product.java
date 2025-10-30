package com.wjc.codetest.product.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category")
    private String category;

    @Column(name = "name")
    private String name;

    protected Product() {
    }

    public Product(
            String category,
            String name
    ) {
        this.category = category;
        this.name = name;
    }
}
