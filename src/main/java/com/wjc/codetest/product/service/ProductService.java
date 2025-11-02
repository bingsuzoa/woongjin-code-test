package com.wjc.codetest.product.service;

import com.wjc.codetest.global.exception.BusinessException;
import com.wjc.codetest.product.controller.dto.request.product.CreateProductRequest;
import com.wjc.codetest.product.controller.dto.request.product.UpdateProductRequest;
import com.wjc.codetest.product.controller.dto.response.product.ProductDto;
import com.wjc.codetest.product.model.domain.category.Category;
import com.wjc.codetest.product.model.domain.product.Product;
import com.wjc.codetest.product.model.domain.product.ProductCode;
import com.wjc.codetest.product.model.domain.product.ProductName;
import com.wjc.codetest.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.wjc.codetest.global.exception.ErrorCode.PRODUCT_ERROR_002;
import static com.wjc.codetest.global.exception.ErrorCode.PRODUCT_ERROR_OO1;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    /// 3-1. 하드코딩된 값 사용
    /// 3-2. 메서드 네이밍 개선 필요
    /// 3-3. 가독성이 떨어지는 조건문
    /// 3-4. Service에서 Controller로 Entity 직접 반환

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
        if (productRepository.existsByProductCode(createProductRequest.productCode())) {
            throw new BusinessException(PRODUCT_ERROR_OO1);
        }
        Category category = categoryService.getCategoryEntity(createProductRequest.categoryId());
        ProductName name = ProductName.from(createProductRequest.name());
        ProductCode code = ProductCode.from(createProductRequest.productCode());
        return getProductDto(productRepository.save(new Product(category, name, code)));
    }

    @Transactional
    public void delete(Long productId) {
        productRepository.delete(getProductEntity(productId));
    }

    @Transactional
    public ProductDto update(UpdateProductRequest updateProductRequest) {
        Product product = getProductEntity(updateProductRequest.id());
        categoryService.updateCategory(product, updateProductRequest.categoryId());
        product.updateName(ProductName.from(updateProductRequest.name()));
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
                product.getCode());
    }
}