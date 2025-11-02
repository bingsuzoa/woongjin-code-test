package com.wjc.codetest.product.controller.dto.request.product;

import jakarta.validation.constraints.NotNull;

public record UpdateProductRequest(
        @NotNull
        Long id,
        @NotNull
        Long categoryId,
        @NotNull
        String name
) {
}
/// 문제: 단순 데이터 전달용 DTO임에도 일반 class로 작성되어 불필요한 코드가 많음
/// DTO는 데이터를 전달하기 위한 용도이므로, record를 사용해 역할에 충실하도록 개선함
