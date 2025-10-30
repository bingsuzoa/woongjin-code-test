package com.wjc.codetest.product.model.domain;

import jakarta.persistence.*;
import lombok.Getter;

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

    public Product(
            Category category,
            String name
    ) {
        this.category = category;
        this.name = name;
        category.addProduct(this);
    }

    public Long getCategoryId() {
        return category.getId();
    }

    public boolean isInCategory(Long categoryId) {
        if(getCategoryId().equals(categoryId)) {
            return true;
        }
        return false;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public Category changeCategory(Category newCategory) {
        category.removeProduct(this.id);
        category = newCategory;
        category.addProduct(this);
        return category;
    }
}
