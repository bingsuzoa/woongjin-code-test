package com.wjc.codetest.product.service;

import com.wjc.codetest.global.exception.BusinessException;
import com.wjc.codetest.product.model.domain.Category;
import com.wjc.codetest.product.controller.dto.request.product.CreateProductRequest;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.controller.dto.request.product.UpdateProductRequest;
import com.wjc.codetest.product.controller.dto.response.product.ProductDto;
import com.wjc.codetest.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.wjc.codetest.global.response.ResponseCode.*;

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

    @Transactional(readOnly = true)
    public ProductDto getProduct(Long productId) {
        return getProductDto(getProductEntity(productId));
    }

    @Transactional(readOnly = true)
    private Product getProductEntity(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            throw new BusinessException(PRODUCT_ERROR_002);
        }
        return productOptional.get();
    }

    @Transactional
    public ProductDto createProduct(CreateProductRequest createProductRequest) {
        if(productRepository.existsByProductCode(createProductRequest.productCode())) {
            throw new BusinessException(PRODUCT_ERROR_OO1);
        }
        Category category = categoryService.getCategoryEntity(createProductRequest.categoryId());
        return getProductDto(productRepository.save(
                new Product(category,
                        createProductRequest.name(),
                        createProductRequest.productCode())));
    }

    @Transactional
    public void delete(Long productId) {
        productRepository.delete(getProductEntity(productId));
    }

    @Transactional
    public ProductDto update(UpdateProductRequest updateProductRequest) {
        Product product = getProductEntity(updateProductRequest.id());
        categoryService.updateCategory(product, updateProductRequest.categoryId());
        product.updateName(updateProductRequest.name());
        return getProductDto(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> getProductsByCategory(Long categoryId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return productRepository.findAllByCategory(categoryId, pageRequest);
    }

    private ProductDto getProductDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getCategoryId(),
                product.getName(),
                product.getProductCode());
    }
}