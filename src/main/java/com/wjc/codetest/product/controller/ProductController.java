package com.wjc.codetest.product.controller;

import com.wjc.codetest.product.controller.dto.request.product.CreateProductRequest;
import com.wjc.codetest.product.controller.dto.request.product.UpdateProductRequest;
import com.wjc.codetest.product.controller.dto.response.product.ProductDto;
import com.wjc.codetest.product.controller.dto.response.product.ProductsResponse;
import com.wjc.codetest.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    /// 2-1. RESTful 원칙 위반
    /// 2-2. HTTP 응답 상태 코드 일관성 부족
    /// 2-3. ApiResponse 기반 공통 응답 포맷 통일
    /// 2-4. Entity 직접 반환 문제
    /// 2-5. 메서드 네이밍 개선 필요

    private final ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long productId) {
        ProductDto product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid  @RequestBody CreateProductRequest request) {
        ProductDto product = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.delete(productId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody UpdateProductRequest request) {
        ProductDto product = productService.update(request);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ProductsResponse> getProductsOfCategory(
            @PathVariable Long categoryId,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<ProductDto> products = productService.getProductsByCategory(categoryId, page, size);
        ProductsResponse response = new ProductsResponse(
                products.getContent(),
                products.getTotalPages(),
                products.getTotalElements(),
                products.getNumber()
        );
        return ResponseEntity.ok(response);
    }
}