package com.wjc.codetest.product.model.domain.product;

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
public class ProductCode {

    @Column(name = "product_code", unique = true, nullable = false, updatable = false)
    private String productCode;

    private ProductCode(String productCode) {
        this.productCode = productCode;
    }

    public static ProductCode from(String productCode) {
        if(!productCode.matches("PRD-[A-Z0-9]{6,10}")) {
            throw new BusinessException(ErrorCode.PRODUCT_ERROR_004);
        }
        return new ProductCode(productCode);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductCode that = (ProductCode) o;
        return Objects.equals(productCode, that.productCode);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(productCode);
    }
}
