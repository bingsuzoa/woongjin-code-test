package com.wjc.codetest.product.model.domain.category;

import com.wjc.codetest.product.model.domain.product.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private CategoryName name;

    @OneToMany(mappedBy = "category")
    private final List<Product> products = new ArrayList<>();

    public Category(CategoryName name) {
        this.name = name;
    }

    public String getName() {
        return name.getCategoryName();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Long productId) {
        products.removeIf(product -> product.getId().equals(productId));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
