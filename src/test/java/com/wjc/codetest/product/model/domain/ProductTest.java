package com.wjc.codetest.product.model.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    Product getProduct(Category category) {
        return new Product(category, "삼겹살", "productCode");
    }

    @Test
    @DisplayName("상품 생성 시 카테고리와의 연관관계가 자동으로 설정된다")
    void createProduct_success() {
        // given
        Category category = new Category("육류");

        // when
        Product product = getProduct(category);
        ReflectionTestUtils.setField(product, "id", 1L);

        // then
        assertThat(product.getCategoryId()).isEqualTo(category.getId());
        assertThat(category.getProducts()).contains(product);
    }

    @Test
    @DisplayName("상품의 카테고리 ID가 올바르게 반환된다")
    void getCategoryId_success() {
        // given
        Category category = new Category("과일");
        ReflectionTestUtils.setField(category, "id", 10L);
        Product product = getProduct(category);

        // when & then
        assertThat(product.getCategoryId()).isEqualTo(10L);
    }

    @Test
    @DisplayName("상품이 특정 카테고리에 속하는지 여부를 정확히 판단한다")
    void isInCategory_success() {
        // given
        Category category = new Category("과일");
        ReflectionTestUtils.setField(category, "id", 5L);
        Product product = getProduct(category);

        // when & then
        assertThat(product.isInCategory(5L)).isTrue();
        assertThat(product.isInCategory(999L)).isFalse();
    }

    @Test
    @DisplayName("상품 이름 변경 시 이름이 정상적으로 업데이트된다")
    void updateName_success() {
        // given
        Category category = new Category("과일");
        Product product = getProduct(category);

        // when
        product.updateName("사과");

        // then
        assertThat(product.getName()).isEqualTo("사과");
    }

    @Test
    @DisplayName("상품의 카테고리를 변경하면 기존 카테고리에서 제거되고 새 카테고리에 추가된다")
    void changeCategory_success() {
        // given
        Category oldCategory = new Category("육류");
        Category newCategory = new Category("채소");

        Product product = new Product(oldCategory, "삼겹살", "productCode");
        ReflectionTestUtils.setField(product, "id", 1L);

        // when
        product.changeCategory(newCategory);

        // then
        assertThat(product.getCategoryId()).isEqualTo(newCategory.getId());
        assertThat(oldCategory.getProducts()).doesNotContain(product);
        assertThat(newCategory.getProducts()).contains(product);
    }
}
