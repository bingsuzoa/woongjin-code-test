package com.wjc.codetest.product.model.domain;

import com.wjc.codetest.global.exception.BusinessException;
import com.wjc.codetest.product.model.domain.category.Category;
import com.wjc.codetest.product.model.domain.category.CategoryName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.*;

public class CategoryNameTest {

    /// ///해피 테스트
    @Test
    @DisplayName("같은 이름의 카테고리는 동일한 카테고리로 인식된다")
    void sameNameCategories_areEqual() {
        // given
        Category category1 = new Category(CategoryName.from("육류"));
        Category category2 = new Category(CategoryName.from("육류"));

        // when & then
        assertThat(category1)
                .isEqualTo(category2)
                .hasSameHashCodeAs(category2);
    }

    @Test
    @DisplayName("이름이 다른 카테고리는 서로 다른 카테고리로 인식된다")
    void differentNameCategories_areNotEqual() {
        // given
        Category category1 = new Category(CategoryName.from("육류"));
        Category category2 = new Category(CategoryName.from("과일"));

        // when & then
        assertThat(category1)
                .isNotEqualTo(category2);
    }

    @Test
    @DisplayName("CategoryName 값 객체는 이름이 같으면 동일하게 판단된다")
    void categoryNameEquality() {
        // given
        CategoryName name1 = CategoryName.from("패션");
        CategoryName name2 = CategoryName.from("패션");

        // when & then
        assertThat(name1)
                .isEqualTo(name2)
                .hasSameHashCodeAs(name2);
    }

    @Test
    @DisplayName("카테고리 이름이 유효한 길이일 경우 예외가 발생하지 않는다")
    void validateCategoryName_validLength_success() {
        // given
        String validName = "식품";

        // when & then
        assertThatCode(() -> CategoryName.from(validName))
                .doesNotThrowAnyException();
    }

    /// ///예외 테스트
    @Test
    @DisplayName("카테고리 이름이 너무 짧으면 예외가 발생한다")
    void validateCategoryName_tooShort_fail() {
        // given
        String shortName = "";

        // when & then
        assertThatThrownBy(() -> CategoryName.from(shortName))
                .isInstanceOf(BusinessException.class)
                .hasMessage("CATEGORY_ERROR_003");
    }

    @Test
    @DisplayName("카테고리 이름이 너무 길면 예외가 발생한다")
    void validateCategoryName_tooLong_fail() {
        // given
        String longName = "ABCDEFGHIJK";

        // when & then
        assertThatThrownBy(() -> CategoryName.from(longName))
                .isInstanceOf(BusinessException.class)
                .hasMessage("CATEGORY_ERROR_003");
    }
}
