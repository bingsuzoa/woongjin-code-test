package com.wjc.codetest.product.repository;

import com.wjc.codetest.product.controller.dto.response.category.CategoryDto;
import com.wjc.codetest.product.model.domain.category.Category;
import com.wjc.codetest.product.model.domain.category.CategoryName;
import com.wjc.codetest.product.model.domain.product.Product;
import com.wjc.codetest.product.model.domain.product.ProductCode;
import com.wjc.codetest.product.model.domain.product.ProductName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.stream.IntStream;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryPerformanceTest {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Category category = categoryRepository.save(
                    new Category(CategoryName.from("카테고리" + i))
            );

            IntStream.rangeClosed(1, 10).forEach(j -> {
                String code = String.format("PRD-%03d%03d", i, j);
                Product product = new Product(
                        category,
                        ProductName.from("상품" + j),
                        ProductCode.from(code)
                );
                productRepository.save(product);
            });
        });
    }

    @Test
    @DisplayName("엔티티 조회 후 DTO 변환 vs DTO 직접 조회 성능 비교")
    void compareEntityVsDtoPerformance() {
        long startEntity = System.currentTimeMillis();

        List<Category> categories = categoryRepository.getCategoriesOfRegisteredProducts();
        List<CategoryDto> dto1 = categories.stream()
                .map(c -> new CategoryDto(c.getId(), c.getName()))
                .toList();
        long endEntity = System.currentTimeMillis();
        System.out.println("Entity → DTO 변환 시간: " + (endEntity - startEntity) + " ms");

        long startDto = System.currentTimeMillis();
        List<CategoryDto> dto2 = categoryRepository.findAllWithProductsDto();
        long endDto = System.currentTimeMillis();
        System.out.println("DTO 직접 조회 시간: " + (endDto - startDto) + " ms");

        System.out.println("성능 차이: " + ((endEntity - startEntity) - (endDto - startDto)) + " ms");
        System.out.println("Entity 기반 DTO 크기: " + dto1.size() + ", Direct DTO 크기: " + dto2.size());
    }
}
