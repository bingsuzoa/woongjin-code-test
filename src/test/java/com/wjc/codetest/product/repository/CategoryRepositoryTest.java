package com.wjc.codetest.product.repository;

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
        Category cat1 = new Category(CategoryName.from("식품"));
        Category cat2 = new Category(CategoryName.from("가전"));
        Category cat3 = new Category(CategoryName.from("유제품"));

        em.persist(cat1);
        em.persist(cat2);
        em.persist(cat3);

        Product p1 = new Product(
                cat1,
                ProductName.from("과자"),
                ProductCode.from("PRD-ABC123")
        );
        Product p2 = new Product(
                cat2,
                ProductName.from("냉장고"),
                ProductCode.from("PRD-XYZ999")
        );
        em.persist(p1);
        em.persist(p2);
        em.flush();
        em.clear();

        // when
        List<Category> result = categoryRepository.getCategoriesOfRegisteredProducts();

        // then
        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(c -> c.getName())
                .containsExactlyInAnyOrder("식품", "가전");

        assertThat(result.get(0).getProducts()).isNotEmpty();
        assertThat(result.get(1).getProducts()).isNotEmpty();
    }

    @Test
    @DisplayName("상품이 없는 카테고리는 조회되지 않는다")
    void getCategoriesOfRegisteredProducts_excludeEmptyCategory() {
        // given
        Category cat1 = new Category(CategoryName.from("식품"));
        Category cat2 = new Category(CategoryName.from("패션"));
        em.persist(cat1);
        em.persist(cat2);

        Product p1 = new Product(
                cat1,
                ProductName.from("과자"),
                ProductCode.from("PRD-AAA111")
        );
        em.persist(p1);
        em.flush();
        em.clear();

        // when
        List<Category> result = categoryRepository.getCategoriesOfRegisteredProducts();

        // then
        assertThat(result)
                .extracting(c -> c.getName())
                .containsExactly("식품")
                .doesNotContain("패션");
    }
}