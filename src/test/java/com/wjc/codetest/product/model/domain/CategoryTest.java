package com.wjc.codetest.product.model.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class CategoryTest {

    @Test
    @DisplayName("상품 추가 시 카테고리의 상품 목록에 정상적으로 추가된다")
    void addProduct_success() {
        // given
        Category category = new Category("육류");
        Product product = new Product(category, "삼겹살");
        ReflectionTestUtils.setField(product, "id", 1L);

        // then
        assertThat(category.getProducts()).hasSize(1);
        assertThat(category.getProducts().get(0).getName()).isEqualTo("삼겹살");
        assertThat(category.getProducts().get(0).getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("상품 제거 시 해당 상품이 카테고리에서 정상적으로 삭제된다")
    void removeProduct_success() {
        // given
        Category category = new Category("육류");
        Product p1 = new Product(category, "삼겹살");
        Product p2 = new Product(category, "소고기");
        ReflectionTestUtils.setField(p1, "id", 1L);
        ReflectionTestUtils.setField(p2, "id", 2L);

        // when
        category.removeProduct(1L);

        // then
        assertThat(category.getProducts())
                .hasSize(1)
                .extracting(Product::getName)
                .containsExactly("소고기");
    }

    @Test
    @DisplayName("상품 추가/삭제 후 products 리스트 상태가 일관되게 유지된다")
    void productsConsistency_afterAddAndRemove() {
        // given
        Category category = new Category("과일");
        Product p1 = new Product(category, "사과");
        Product p2 = new Product(category, "배");
        ReflectionTestUtils.setField(p1, "id", 1L);
        ReflectionTestUtils.setField(p2, "id", 2L);

        // when
        category.removeProduct(1L);

        // then
        assertThat(category.getProducts()).hasSize(1);
        assertThat(category.getProducts().get(0).getName()).isEqualTo("배");
    }
}
