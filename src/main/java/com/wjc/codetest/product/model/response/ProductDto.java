package com.wjc.codetest.product.model.response;


public record ProductDto(
        Long id,
        Long categoryId,
        String name
) {
}
