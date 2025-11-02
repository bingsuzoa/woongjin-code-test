package com.wjc.codetest.product.model.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Product {
    /// 1-1. Product 생성 시 Category 연관관계 설정 로직 개선
    /// 1-2. Product에 고유 식별 코드(productCode) 추가

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "product_code", unique = true, nullable = false, updatable = false)
    private String productCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    protected Product() {
    }

    public Product(
            Category category,
            String name,
            String productCode
    ) {
        this.category = category;
        this.name = name;
        this.productCode = productCode;
        category.addProduct(this);
    }

    public Long getCategoryId() {
        return category.getId();
    }

    public boolean isInCategory(Long categoryId) {
        return getCategoryId().equals(categoryId);
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
