package com.wjc.codetest.product.repository;

import com.wjc.codetest.product.controller.dto.response.product.ProductDto;
import com.wjc.codetest.product.model.domain.Category;
import com.wjc.codetest.product.model.domain.Product;
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
        Category food = categoryRepository.save(new Category("식품"));
        Category electronics = categoryRepository.save(new Category("가전"));

        // 식품 카테고리에 상품 15개 등록
        IntStream.rangeClosed(1, 15)
                .forEach(i -> {
                    String code = "FOOD-" + i;
                    productRepository.save(new Product(food, "식품상품" + i, code));
                });

        // 가전 카테고리에 상품 5개 등록
        IntStream.rangeClosed(1, 5)
                .forEach(i -> {
                    String code = "ELEC-" + i;
                    productRepository.save(new Product(electronics, "가전상품" + i, code));
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
        Category emptyCategory = new Category("빈카테고리");
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