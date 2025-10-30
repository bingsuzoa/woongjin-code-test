package com.wjc.codetest.product.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Lazy;

@Entity
@Getter
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    protected Product() {
    }

    public Product(String name) {
        this.name = name;
    }

    public void assignCategory(Category category) {
        this.category = category;
        category.addProduct(this);
    }

    public Long getCategoryId() {
        return category.getId();
    }
}
