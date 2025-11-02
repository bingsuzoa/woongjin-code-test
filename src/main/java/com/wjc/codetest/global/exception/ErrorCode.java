package com.wjc.codetest.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    /// Product
    PRODUCT_ERROR_OO1(400, "PRODUCT_ERROR_001", "이미 등록된 상품을 등록 시도함"),
    PRODUCT_ERROR_002(400, "PRODUCT_ERROR_002", "존재하지 않는 상품을 찾으려고 시도함"),
    PRODUCT_ERROR_003(400, "PRODUCT_ERROR_003", "상품 이름이 규칙에 맞지 않음"),
    PRODUCT_ERROR_004(400, "PRODUCT_ERROR_004", "상품 코드가 규칙에 맞지 않음"),

    /// Category
    CATEGORY_ERROR_001(400, "CATEGORY_ERROR_001", "이미 등록된 카테고리를 등록 시도함"),
    CATEGORY_ERROR_002(400, "CATEGORY_ERROR_002", "존재하지 않는 카테고리를 찾으려고 시도함"),
    CATEGORY_ERROR_003(400, "CATEGORY_ERROR_003", "카테고리 이름이 규칙에 맞지 않음"),

    /// Common
    VALIDATION_ERROR(400, "VALIDATION_ERROR", "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "예상하지 못한 런타임 예외");

    private final int statusCode;
    private final String errorCode;
    private final String message;

    ErrorCode(
            int statusCode,
            String errorCode,
            String message
    ) {
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.message = message;
    }
}
