package com.wjc.codetest.product.repository;

import com.wjc.codetest.product.model.domain.Category;
import com.wjc.codetest.product.model.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    Page<Product> findAllByCategory(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query("SELECT DISTINCT category FROM product")
    List<Category> getCategoriesOfRegisteredProducts();
}
