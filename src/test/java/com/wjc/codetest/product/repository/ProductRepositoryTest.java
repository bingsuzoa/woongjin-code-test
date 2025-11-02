package com.wjc.codetest.product.repository;

import com.wjc.codetest.product.controller.dto.response.product.ProductDto;
import com.wjc.codetest.product.model.domain.category.Category;
import com.wjc.codetest.product.model.domain.category.CategoryName;
import com.wjc.codetest.product.model.domain.product.Product;
import com.wjc.codetest.product.model.domain.product.ProductCode;
import com.wjc.codetest.product.model.domain.product.ProductName;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("특정 카테고리의 상품을 페이징 조회한다")
    void findAllByCategory_success() {
        // given
        Category food = categoryRepository.save(new Category(CategoryName.from("식품")));
        Category electronics = categoryRepository.save(new Category(CategoryName.from("가전")));

        IntStream.rangeClosed(1, 15)
                .forEach(i -> {
                    String code = String.format("PRD-FOOD%04d", i);
                    Product product = new Product(
                            food,
                            ProductName.from("식품" + i),
                            ProductCode.from(code)
                    );
                    productRepository.save(product);
                });

        IntStream.rangeClosed(1, 5)
                .forEach(i -> {
                    String code = String.format("PRD-ELEC%04d", i);
                    Product product = new Product(
                            electronics,
                            ProductName.from("가전" + i),
                            ProductCode.from(code)
                    );
                    productRepository.save(product);
                });

        em.flush();
        em.clear();

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<ProductDto> result = productRepository.findAllByCategory(food.getId(), pageable);

        // then
        assertThat(result.getContent().size()).isEqualTo(10);
        assertThat(result.getTotalElements()).isEqualTo(15);
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.getContent().get(0).categoryId()).isEqualTo(food.getId());
    }

    @Test
    @DisplayName("상품이 없는 카테고리 조회 시 빈 페이지 반환")
    void findAllByCategory_emptyResult() {
        // given
        Category emptyCategory = new Category(CategoryName.from("빈카테고리"));
        em.persist(emptyCategory);
        em.flush();
        em.clear();

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<ProductDto> result = productRepository.findAllByCategory(emptyCategory.getId(), pageable);

        // then
        assertThat(result.getContent().size()).isZero();
        assertThat(result.getTotalElements()).isZero();
        assertThat(result.getTotalPages()).isZero();
    }
}