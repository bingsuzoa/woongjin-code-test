package com.wjc.codetest.product.model.domain.product;

import com.wjc.codetest.product.model.domain.category.Category;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    /// 1-1. Product 생성 시 Category 연관관계 설정 로직 개선
    /// 1-2. Product에 고유 식별 코드(productCode) 추가

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ProductName name;

    @Embedded
    private ProductCode code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public Product(
            Category category,
            ProductName productName,
            ProductCode productCode
    ) {
        this.category = category;
        this.name = productName;
        this.code = productCode;
        category.addProduct(this);
    }

    public Long getCategoryId() {
        return category.getId();
    }

    public boolean isInCategory(Long categoryId) {
        return getCategoryId().equals(categoryId);
    }

    public void updateName(ProductName newName) {
        this.name = newName;
    }

    public String getCode() {
        return code.getProductCode();
    }

    public String getName() {
        return name.getProductName();
    }

    public Category changeCategory(Category newCategory) {
        category.removeProduct(this.id);
        category = newCategory;
        category.addProduct(this);
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(code, product.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }
}
