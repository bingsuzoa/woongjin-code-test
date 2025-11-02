package com.wjc.codetest.product.model.domain;

import com.wjc.codetest.global.exception.BusinessException;
import com.wjc.codetest.product.model.domain.product.ProductName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.*;

public class ProductNameTest {

    @Test
    @DisplayName("유효한 이름 길이(1~30)면 ProductName 객체가 정상 생성된다")
    void createProductName_success() {
        // given
        String validName = "돼지고기삼겹살";

        // when
        ProductName productName = ProductName.from(validName);

        // then
        assertThat(productName).isNotNull();
        assertThat(productName.getProductName()).isEqualTo(validName);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "alksfjlaurpeajflkajflkadjflajfljalfejrajf"})
    @DisplayName("이름이 너무 짧거나 길면 예외가 발생한다")
    void createProductName_invalid_fail(String invalidName) {
        // when & then
        assertThatThrownBy(() -> ProductName.from(invalidName))
                .isInstanceOf(BusinessException.class)
                .hasMessage("PRODUCT_ERROR_003");
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "ab", "abcd"}) // 1자~28자 (정상 범위)
    @DisplayName("이름이 규칙 내라면 예외 없이 생성된다")
    void createProductName_valid_pass(String validName) {
        // when & then
        assertThatCode(() -> ProductName.from(validName))
                .doesNotThrowAnyException();
    }
}
