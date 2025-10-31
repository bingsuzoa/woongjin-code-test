package com.wjc.codetest.product.repository;

import com.wjc.codetest.product.model.domain.Category;
import com.wjc.codetest.product.model.domain.Product;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("등록된 상품이 있는 카테고리만 조회한다 (JOIN FETCH 테스트)")
    void getCategoriesOfRegisteredProducts_success() {
        // given
        Category cat1 = new Category("식품");
        Category cat2 = new Category("가전");
        Category cat3 = new Category("유제품");
        em.persist(cat1);
        em.persist(cat2);
        em.persist(cat3);

        Product p1 = new Product(cat1, "과자", "productCode1");
        Product p2 = new Product(cat2, "냉장고", "productCode2");
        em.persist(p1);
        em.persist(p2);
        em.flush();
        em.clear();

        // when
        List<Category> result = categoryRepository.getCategoriesOfRegisteredProducts();

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getProducts()).isNotEmpty();
        assertThat(result.get(1).getProducts()).isNotEmpty();
    }

    @Test
    @DisplayName("상품이 없는 카테고리는 조회되지 않는다")
    void getCategoriesOfRegisteredProducts_excludeEmptyCategory() {
        // given
        Category cat1 = new Category("식품");
        Category cat2 = new Category("패션");
        em.persist(cat1);
        em.persist(cat2);

        Product p1 = new Product(cat1, "과자", "productCode1");
        em.persist(p1);
        em.flush();
        em.clear();

        // when
        List<Category> result = categoryRepository.getCategoriesOfRegisteredProducts();

        // then
        assertThat(result)
                .extracting(Category::getName)
                .containsExactly("식품")
                .doesNotContain("패션");
    }
}