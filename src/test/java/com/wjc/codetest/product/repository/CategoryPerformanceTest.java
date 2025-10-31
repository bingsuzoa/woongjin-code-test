package com.wjc.codetest.product.repository;

import com.wjc.codetest.product.controller.dto.response.category.CategoryDto;
import com.wjc.codetest.product.model.domain.Category;
import com.wjc.codetest.product.model.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
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
        IntStream.rangeClosed(1, 1000).forEach(i -> {
            Category c = categoryRepository.save(new Category("카테고리" + i));

            IntStream.rangeClosed(1, 10).forEach(j -> {
                String code = String.format("P-%04d-%03d", i, j);
                Product p = new Product(c, "상품" + j, code);
                productRepository.save(p);
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
