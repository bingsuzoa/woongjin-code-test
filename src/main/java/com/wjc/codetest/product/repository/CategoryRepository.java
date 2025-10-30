package com.wjc.codetest.product.repository;

import com.wjc.codetest.product.controller.dto.response.category.CategoryDto;
import com.wjc.codetest.product.model.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT DISTINCT c FROM Category c JOIN FETCH c.products")
    List<Category> getCategoriesOfRegisteredProducts();

    @Query("SELECT DISTINCT new com.wjc.codetest.product.controller.dto.response.category.CategoryDto(c.id, c.name) " +
            "FROM Category c JOIN c.products p")
    List<CategoryDto> findAllWithProductsDto();
}
