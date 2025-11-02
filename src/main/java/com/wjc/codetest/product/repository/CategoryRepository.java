package com.wjc.codetest.product.repository;

import com.wjc.codetest.product.controller.dto.response.category.CategoryDto;
import com.wjc.codetest.product.model.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE c.name.categoryName = :name")
    boolean existsByName(@Param("name") String name);

    @Query("SELECT DISTINCT c FROM Category c JOIN FETCH c.products")
    List<Category> getCategoriesOfRegisteredProducts();

    @Query("""
        SELECT DISTINCT new com.wjc.codetest.product.controller.dto.response.category.CategoryDto(
            c.id,
            c.name.categoryName
        )
        FROM Category c
        JOIN c.products p
    """)
    List<CategoryDto> findAllWithProductsDto();
}
