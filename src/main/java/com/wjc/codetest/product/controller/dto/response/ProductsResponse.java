package com.wjc.codetest.product.controller.dto.response;

import com.wjc.codetest.product.controller.dto.response.product.ProductDto;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author : 변영우 byw1666@wjcompass.com
 * @since : 2025-10-27
 */
public record ProductsResponse(
        List<ProductDto> products,
        int totalPages,
        long totalElements,
        int page
) {

}