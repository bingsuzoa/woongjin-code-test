package com.wjc.codetest.product.controller.dto.request.product;


public record CreateProductRequest(
        Long categoryId,
        String name,
        String productCode
) {
}
/// 문제: 단순 데이터 전달용 DTO임에도 일반 class로 작성되어 불필요한 코드가 많음