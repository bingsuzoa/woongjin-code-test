package com.wjc.codetest.product.model.domain.product;

import com.wjc.codetest.global.exception.BusinessException;
import com.wjc.codetest.global.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductName {

    @Column(name = "product_name", nullable = false, length = 30)
    private String productName;

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 30;

    private ProductName(String productName) {
        this.productName = productName;
    }

    public static ProductName from(String name) {
        validate(name);
        return new ProductName(name);
    }

    private static void validate(String name) {
        if (name.length() < MIN_LENGTH || name.length() > MAX_LENGTH) {
            throw new BusinessException(ErrorCode.PRODUCT_ERROR_003);
        }
    }
}
