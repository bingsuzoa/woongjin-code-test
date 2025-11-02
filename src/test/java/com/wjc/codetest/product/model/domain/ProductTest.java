package com.wjc.codetest.product.model.domain;

import com.wjc.codetest.global.exception.BusinessException;
import com.wjc.codetest.product.model.domain.category.Category;
import com.wjc.codetest.product.model.domain.category.CategoryName;
import com.wjc.codetest.product.model.domain.product.Product;
import com.wjc.codetest.product.model.domain.product.ProductCode;
import com.wjc.codetest.product.model.domain.product.ProductName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ProductTest {

    private Category createCategory(String name) {
        return new Category(CategoryName.from(name));
    }

    private Product createProduct(Category category, String name, String code) {
        return new Product(category, ProductName.from(name), ProductCode.from(code));
    }

    //////해피 테스트

    @Test
    @DisplayName("상품 생성 시 카테고리의 products 리스트에 자동 추가된다")
    void addProductToCategory_success() {
        // given
        Category category = createCategory("식품");

        // when
        Product product = createProduct(category, "초코파이", "PRD-ABC123");

        // then
        assertThat(category.getProducts()).hasSize(1);
        assertThat(category.getProducts().get(0)).isEqualTo(product);
        assertThat(category.getProducts().get(0).getName()).isEqualTo("초코파이");
    }

    @Test
    @DisplayName("카테고리 변경 시 기존/새 카테고리 모두 일관성 있게 유지된다")
    void changeCategory_success() {
        // given
        Category oldCategory = createCategory("식품");
        Category newCategory = createCategory("가전");

        Product product = createProduct(oldCategory, "초코파이", "PRD-AAA111");
        ReflectionTestUtils.setField(product, "id", 1L);

        // when
        product.changeCategory(newCategory);

        // then
        assertThat(oldCategory.getProducts()).isEmpty();
        assertThat(newCategory.getProducts()).containsExactly(product);
        assertThat(product.getCategoryId()).isEqualTo(newCategory.getId());
    }

    @Test
    @DisplayName("상품 이름 변경 시 정상 반영된다")
    void updateName_success() {
        // given
        Category category = createCategory("식품");
        Product product = createProduct(category, "초코파이", "PRD-AAA111");

        // when
        ProductName newName = ProductName.from("오예스");
        product.updateName(newName);

        // then
        assertThat(product.getName()).isEqualTo("오예스");
    }

    @Test
    @DisplayName("상품 코드가 같으면 동일 상품으로 간주된다")
    void equals_sameCode_true() {
        // given
        Category category = createCategory("식품");
        Product p1 = createProduct(category, "초코파이", "PRD-AAA111");
        Product p2 = createProduct(category, "다이제", "PRD-AAA111");

        // when & then
        assertThat(p1).isEqualTo(p2);
        assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
    }

    @Test
    @DisplayName("상품 코드가 다르면 다른 상품으로 간주된다")
    void equals_differentCode_false() {
        // given
        Category category = createCategory("식품");
        Product p1 = createProduct(category, "초코파이", "PRD-AAA111");
        Product p2 = createProduct(category, "초코파이", "PRD-BBB222");

        // when & then
        assertThat(p1).isNotEqualTo(p2);
    }

    /// ///예외 테스트
    @ParameterizedTest
    @DisplayName("잘못된 형식의 상품코드면 예외가 발생한다")
    @ValueSource(strings = {"", "PRD-", "prd-ABC123", "PRD-ABC", "PRD-ABCDEFGHIJKL123" })
    void createProductCode_invalid_fail(String invalidCode) {
        assertThatThrownBy(() -> ProductCode.from(invalidCode))
                .isInstanceOf(BusinessException.class)
                .hasMessage("PRODUCT_ERROR_004");
    }

    @ParameterizedTest
    @DisplayName("상품명이 너무 짧거나 길면 예외가 발생한다")
    @ValueSource(strings = {"", "fjadlfjaelfjalfjaorjlaksnalufpewmgf,dshcif;emfds"})
    void createProductName_invalid_fail(String invalidName) {
        assertThatThrownBy(() -> ProductName.from(invalidName))
                .isInstanceOf(BusinessException.class)
                .hasMessage("PRODUCT_ERROR_003");
    }
}