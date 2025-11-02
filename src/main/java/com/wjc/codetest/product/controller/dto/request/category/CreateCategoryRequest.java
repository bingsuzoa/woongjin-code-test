package com.wjc.codetest.product.controller.dto.request.category;

import jakarta.validation.constraints.NotNull;

public record CreateCategoryRequest(
        @NotNull
        String name
) {
}
