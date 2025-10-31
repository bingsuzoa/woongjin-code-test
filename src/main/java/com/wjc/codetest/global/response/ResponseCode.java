package com.wjc.codetest.global.response;

public enum ResponseCode {

    /// Product
    PRODUCT_SUCCESS("PRODUCT_SUCCESS", "상품 생성 성공"),
    PRODUCT_ERROR_OO1("PRODUCT_ERROR_001", "이미 등록된 상품을 등록 시도함"),
    PRODUCT_ERROR_002("PRODUCT_ERROR_002", "존재하지 않는 상품을 찾으려고 시도함"),

    /// Category
    CATEGORY_SUCCESS("CATEGORY_SUCCESS", "카테고리 생성 성공"),
    CATEGORY_ERROR_001("CATEGORY_ERROR_001", "이미 등록된 카테고리를 등록 시도함"),
    CATEGORY_ERROR_002("CATEGORY_ERROR_002", "존재하지 않는 카테고리를 찾으려고 시도함"),

    /// Common
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "예상하지 못한 런타임 예외");

    private final String code;
    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
