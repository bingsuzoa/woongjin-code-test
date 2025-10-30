package com.wjc.codetest.product.controller;

import com.wjc.codetest.product.controller.dto.request.product.CreateProductRequest;
import com.wjc.codetest.product.controller.dto.request.GetProductListRequest;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.controller.dto.request.product.UpdateProductRequest;
import com.wjc.codetest.product.controller.dto.response.ProductDto;
import com.wjc.codetest.product.controller.dto.response.ProductListResponse;
import com.wjc.codetest.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    /// 문제 1 : RESTful API 설계 원칙에 맞지 않음
    /// RESTful API는 자원중심으로 설계, 행동은 HTTP Method 통해 표현해야 함
    /// 현재 구현은 이러한 일관된 의사소통 규칙을 따르지 않아 RESTful하지 않음

    /// 문제 2 : Entity을 직접 반환하고 있음
    /// 객체를 JSON으로 바꾸는 과정에서 연관된 엔티티를 재귀적으로 탐색함
    /// Product(1) → Category(N) → Product(1)로 순환 참조가 발생할 수 있음

    /// 문제 3 : getProductById 메서드 네이밍이 객체지향적인 코드가 아님
    /// 데이터 접근이 아닌 행위가 드러나도록 변경해야 함

    /// 문제 4: 요청 DTO의 이름이 단순히 dto로 작성되어 의도를 파악하기 어려움
    /// DTO의 용도를 명확히 드러내도록 네이밍을 개선해야 함

    private final ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable(name = "productId") Long productId){
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody CreateProductRequest createProductRequest){
        return ResponseEntity.ok(productService.createProduct(createProductRequest));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable(name = "productId") Long productId){
        productService.delete(productId);
        return ResponseEntity.ok(true);
    }

    /// put(전체 수정) vs patch(일부 수정)
    /// 일부 수정에도 PUT 선택한 이유 : 클라이언트 - 서버간 데이터 전달에 일관성을 유지하기 위해 선택
    @PutMapping
    public ResponseEntity<ProductDto> updateProduct(@RequestBody UpdateProductRequest updateProductRequest){
        return ResponseEntity.ok(productService.update(updateProductRequest));
    }

    @PostMapping(value = "/product/list")
    public ResponseEntity<ProductListResponse> getProductListByCategory(@RequestBody GetProductListRequest dto){
        Page<Product> productList = productService.getListByCategory(dto);
        return ResponseEntity.ok(new ProductListResponse(productList.getContent(), productList.getTotalPages(), productList.getTotalElements(), productList.getNumber()));
    }

    @GetMapping(value = "/product/category/list")
    public ResponseEntity<List<String>> getProductListByCategory(){
        List<String> uniqueCategories = productService.getUniqueCategories();
        return ResponseEntity.ok(uniqueCategories);
    }
}