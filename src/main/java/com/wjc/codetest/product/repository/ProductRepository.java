package com.wjc.codetest.product.repository;

import com.wjc.codetest.product.controller.dto.response.product.ProductDto;
import com.wjc.codetest.product.model.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT COUNT(p) > 0 FROM Product p WHERE p.productCode = :productCode")
    boolean existsByProductCode(@Param("productCode") String productCode);

    @Query("""
                SELECT new com.wjc.codetest.product.controller.dto.response.product.ProductDto(
                    p.id,
                    p.category.id,
                    p.name,
                    p.productCode
                )
                FROM Product p
                WHERE p.category.id = :categoryId
            """)
    Page<ProductDto> findAllByCategory(@Param("categoryId") Long categoryId, Pageable pageable);
}
