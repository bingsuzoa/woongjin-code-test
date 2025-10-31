package com.wjc.codetest.product.controller.dto.response.product;


public record ProductDto(
        Long id,
        Long categoryId,
        String name,
        String productCode
) {
}
