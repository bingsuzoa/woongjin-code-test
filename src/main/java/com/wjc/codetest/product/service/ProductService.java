package com.wjc.codetest.product.service;

import com.wjc.codetest.product.model.domain.Category;
import com.wjc.codetest.product.controller.dto.request.product.CreateProductRequest;
import com.wjc.codetest.product.controller.dto.request.GetProductListRequest;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.controller.dto.request.product.UpdateProductRequest;
import com.wjc.codetest.product.controller.dto.response.ProductDto;
import com.wjc.codetest.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    /// 문제 1: 코드 내부에 하드코딩된 값이 존재함
    /// 값이 코드에 직접 작성되어 있어 수정 시 직접 찾아야 하며 유지보수가 어려움

    /// 문제 2 : getProductById 메서드 네이밍이 객체지향적인 코드가 아님
    /// 데이터 접근이 아닌 행위가 드러나도록 변경해야 함

    /// 문제 3 : !productOptional.isPresent() 가독성이 떨어짐
    /// productOptional.isEmpty()로 직관적인 표현으로 변경해야 함

    /// 문제 4 : Service에서 Controller로 Entity 직접 반환
    /// Service에서 Controller로 데이터 전달 시 DTO를 사용하여 필요한 데이터만 전달해야 함
    /// -> Controller는 비즈니스 로직을 처리하는 곳이 아니므로 Entity를 전달할 이유가 없음

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public static final String NOT_EXIST_PRODUCT = "product not found";

    public ProductDto getProduct(Long productId) {
        return getProductDto(getProductEntity(productId));
    }

    private Product getProductEntity(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            throw new IllegalArgumentException(NOT_EXIST_PRODUCT);
        }
        return productOptional.get();
    }

    public ProductDto createProduct(CreateProductRequest createProductRequest) {
        Category category = categoryService.getCategoryEntity(createProductRequest.categoryId());
        return getProductDto(productRepository.save(new Product(category, createProductRequest.name())));
    }

    /// tradeOff
    /// 조회 후 삭제(선택) : 비즈니스 로직에서 예외를 명시적으로 처리할 수 있음 / 2번 조회 발생
    /// repository 삭제 : 1번 조회 / 코드 가독성, 일관성 떨어짐
    /// -> id 조회는 index 조회이므로 한번 더 조회한다고 해서 성능에 큰 영향을 주지 않음
    public void delete(Long productId) {
        productRepository.delete(getProductEntity(productId));
    }

    public ProductDto update(UpdateProductRequest updateProductRequest) {
        Product product = getProductEntity(updateProductRequest.id());
        categoryService.updateCategory(product, updateProductRequest.categoryId());
        product.updateName(updateProductRequest.name());
        return getProductDto(product);
    }

    public Page<Product> getListByCategory(GetProductListRequest dto) {
        PageRequest pageRequest = PageRequest.of(dto.getPage(), dto.getSize(), Sort.by(Sort.Direction.ASC, "category"));
        return productRepository.findAllByCategory(dto.getCategory(), pageRequest);
    }

    public List<String> getUniqueCategories() {
        return productRepository.findDistinctCategories();
    }

    private ProductDto getProductDto(Product product) {
        return new ProductDto(product.getId(), product.getCategoryId(), product.getName());
    }
}