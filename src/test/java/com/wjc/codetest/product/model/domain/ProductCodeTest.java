package com.wjc.codetest.product.model.domain;

import com.wjc.codetest.global.exception.BusinessException;
import com.wjc.codetest.product.model.domain.product.ProductCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.*;

public class ProductCodeTest {
    @Test
    @DisplayName("올바른 형식의 productCode면 정상 생성된다")
    void createProductCode_success() {
        // given
        String validCode = "PRD-ABC123";

        // when
        ProductCode productCode = ProductCode.from(validCode);

        // then
        assertThat(productCode).isNotNull();
        assertThat(productCode.getProductCode()).isEqualTo("PRD-ABC123");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "PRD-abc123",
            "PRD-123",
            "PRD-ABCDEFGHIJKL",
            "ABC-123456",
            "PRD_ABC123",
            ""
    })
    @DisplayName("형식이 잘못된 productCode면 예외가 발생한다")
    void createProductCode_invalid_fail(String invalidCode) {
        // when & then
        assertThatThrownBy(() -> ProductCode.from(invalidCode))
                .isInstanceOf(BusinessException.class)
                .hasMessage("PRODUCT_ERROR_004");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "PRD-ABCDEFG",
            "PRD-XYZ7890",
            "PRD-A1B2C3D4"
    })
    @DisplayName("형식이 올바른 productCode면 예외 없이 생성된다")
    void createProductCode_valid_pass(String validCode) {
        // when & then
        assertThatCode(() -> ProductCode.from(validCode))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("동일한 productCode 문자열이면 equals와 hashCode가 동일하다")
    void equalsAndHashCode_sameValue_success() {
        // given
        ProductCode code1 = ProductCode.from("PRD-ABC123");
        ProductCode code2 = ProductCode.from("PRD-ABC123");

        // then
        assertThat(code1)
                .isEqualTo(code2)
                .hasSameHashCodeAs(code2);
    }

    @Test
    @DisplayName("서로 다른 productCode 문자열이면 equals가 false를 반환한다")
    void equalsAndHashCode_differentValue_fail() {
        // given
        ProductCode code1 = ProductCode.from("PRD-ABC123");
        ProductCode code2 = ProductCode.from("PRD-XYZ789");

        // then
        assertThat(code1).isNotEqualTo(code2);
        assertThat(code1.hashCode()).isNotEqualTo(code2.hashCode());
    }

    @Test
    @DisplayName("equals는 null 또는 다른 타입에 대해 false를 반환한다")
    void equals_nullOrDifferentType() {
        // given
        ProductCode code = ProductCode.from("PRD-ABC123");

        // then
        assertThat(code.equals(null)).isFalse();
        assertThat(code.equals("PRD-ABC123")).isFalse();
    }
}
