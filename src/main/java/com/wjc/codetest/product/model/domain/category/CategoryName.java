package com.wjc.codetest.product.model.domain.category;

import com.wjc.codetest.global.exception.BusinessException;
import com.wjc.codetest.global.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryName {

    @Column(name = "category_name", nullable = false, length = 10)
    private String categoryName;

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 10;

    private CategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public static CategoryName from(String name) {
        validate(name);
        return new CategoryName(name);
    }

    private static void validate(String name) {
        if (name.length() < MIN_LENGTH || name.length() > MAX_LENGTH) {
            throw new BusinessException(ErrorCode.CATEGORY_ERROR_003);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CategoryName that = (CategoryName) o;
        return Objects.equals(categoryName, that.categoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(categoryName);
    }
}
