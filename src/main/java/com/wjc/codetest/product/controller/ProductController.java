package com.wjc.codetest.product.controller;

import com.wjc.codetest.global.response.ApiResponse;
import com.wjc.codetest.global.response.ResponseCode;
import com.wjc.codetest.product.controller.dto.request.product.CreateProductRequest;
import com.wjc.codetest.product.controller.dto.request.product.UpdateProductRequest;
import com.wjc.codetest.product.controller.dto.response.product.ProductDto;
import com.wjc.codetest.product.controller.dto.response.product.ProductsResponse;
import com.wjc.codetest.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    /// 문제 1: RESTful 원칙 위반 - 자원 중심 설계 및 HTTP Method 일관성 미흡
    /// 문제 2: Entity 직접 반환으로 인한 순환 참조 위험 (Product ↔ Category)
    /// 문제 3: 메서드 네이밍이 객체지향적이지 않음(getProductById → 행위를 드러내도록 변경 필요)
    /// 문제 4: 모든 응답을 200 OK로 반환함 — 생성 시에는 201 Created + Location 헤더로 반환 필요

    private final ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDto>> getProduct(@PathVariable(name = "productId") Long productId) {
        ProductDto product = productService.getProduct(productId);
        return ApiResponse.success(ResponseCode.PRODUCT_SUCCESS, HttpStatus.OK, product);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(@RequestBody CreateProductRequest createProductRequest) {
        ProductDto product = productService.createProduct(createProductRequest);
        return ApiResponse.success(ResponseCode.PRODUCT_SUCCESS, HttpStatus.CREATED, product);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long productId) {
        productService.delete(productId);
        return ApiResponse.success(ResponseCode.PRODUCT_SUCCESS, HttpStatus.OK, null);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(@RequestBody UpdateProductRequest updateProductRequest) {
        ProductDto product = productService.update(updateProductRequest);
        return ApiResponse.success(ResponseCode.PRODUCT_SUCCESS, HttpStatus.OK, product);

    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<ProductsResponse>> getProductsOfCategory(
            @PathVariable Long categoryId,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<ProductDto> products = productService.getProductsByCategory(categoryId, page, size);
        ProductsResponse response = new ProductsResponse(products.getContent(), products.getTotalPages(), products.getTotalElements(), products.getNumber());
        return ApiResponse.success(ResponseCode.PRODUCT_SUCCESS, HttpStatus.OK, response);
    }
}