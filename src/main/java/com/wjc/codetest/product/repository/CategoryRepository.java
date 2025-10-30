package com.wjc.codetest.product.repository;

import com.wjc.codetest.product.model.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findById(Long categoryId);

    @Query("SELECT DISTINCT c FROM Category c JOIN FETCH c.products")
    List<Category> getCategoriesOfRegisteredProducts();
}
