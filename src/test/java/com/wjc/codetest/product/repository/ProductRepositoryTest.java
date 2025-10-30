package com.wjc.codetest.product.repository;

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
    private EntityManager em;

    @Test
    @DisplayName("특정 카테고리의 상품을 페이징 조회한다")
    void findAllByCategory_success() {
        // given
        Category category1 = new Category("식품");
        Category category2 = new Category("가전");
        em.persist(category1);
        em.persist(category2);

        IntStream.rangeClosed(1, 15)
                .forEach(i -> em.persist(new Product(category1, "상품" + i)));
        IntStream.rangeClosed(1, 5)
                .forEach(i -> em.persist(new Product(category2, "가전상품" + i)));
        em.flush();
        em.clear();

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Product> result = productRepository.findAllByCategory(category1.getId(), pageable);

        // then
        assertThat(result.getContent().size()).isEqualTo(10);
        assertThat(result.getTotalElements()).isEqualTo(15);
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.getContent().get(0).getCategory().getId())
                .isEqualTo(category1.getId());
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
        Page<Product> result = productRepository.findAllByCategory(emptyCategory.getId(), pageable);

        // then
        assertThat(result.getContent().size()).isZero();
        assertThat(result.getTotalElements()).isZero();
        assertThat(result.getTotalPages()).isZero();
    }
}