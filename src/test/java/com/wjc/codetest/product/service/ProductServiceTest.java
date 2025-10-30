package com.wjc.codetest.product.service;

import com.wjc.codetest.product.controller.dto.request.product.CreateProductRequest;
import com.wjc.codetest.product.controller.dto.request.product.UpdateProductRequest;
import com.wjc.codetest.product.controller.dto.response.product.ProductDto;
import com.wjc.codetest.product.model.domain.Category;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductService productService;

    private static final String PRODUCT_NAME = "삼겹살";

    /// ///해피 테스트
    @Test
    @DisplayName("상품 단건 조회 성공")
    void getProduct_success() {
        // given
        Category category = new Category("육류");
        Product product = new Product(category, PRODUCT_NAME);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // when
        ProductDto result = productService.getProduct(1L);

        // then
        assertThat(result.name()).isEqualTo(PRODUCT_NAME);
        verify(productRepository).findById(1L);
    }

    @Test
    @DisplayName("상품 생성 성공 시 ProductDto 반환")
    void createProduct_success() {
        // given
        CreateProductRequest request = new CreateProductRequest(1L, PRODUCT_NAME);
        Category category = new Category("육류");
        Product product = new Product(category, PRODUCT_NAME);

        when(categoryService.getCategoryEntity(1L)).thenReturn(category);
        when(productRepository.save(any())).thenReturn(product);

        // when
        ProductDto result = productService.createProduct(request);

        // then
        assertThat(result.name()).isEqualTo(PRODUCT_NAME);
        verify(categoryService).getCategoryEntity(1L);
    }

    @Test
    @DisplayName("상품 삭제 시 productRepository.delete() 호출됨")
    void deleteProduct_success() {
        // given
        Category category = new Category("육류");
        Product product = new Product(category, PRODUCT_NAME);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // when
        productService.delete(1L);

        // then
        verify(productRepository).delete(product);
    }

    @Test
    @DisplayName("상품 수정 시 이름 및 카테고리 변경")
    void updateProduct_success() {
        // given
        Category category1 = new Category("육류");
        Category category2 = new Category("과일");
        Product product = new Product(category1, PRODUCT_NAME);

        UpdateProductRequest request = new UpdateProductRequest(1L, 2L, "사과");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryService.updateCategory(product, 2L)).thenReturn(category2);

        // when
        ProductDto result = productService.update(request);

        // then
        assertThat(result.name()).isEqualTo("사과");
        verify(categoryService).updateCategory(product, 2L);
    }

    @Test
    @DisplayName("카테고리별 상품 페이징 조회 성공")
    void getProductsByCategory_success() {
        // given
        ProductDto p1 = new ProductDto(1L, 1L, "소고기");
        ProductDto p2 = new ProductDto(2L, 1L, "닭고기");

        Page<ProductDto> productPage = new PageImpl<>(List.of(p1, p2));
        when(productRepository.findAllByCategory(eq(1L), any(Pageable.class))).thenReturn(productPage);

        // when
        Page<ProductDto> result = productService.getProductsByCategory(1L, 0, 10);

        // then
        assertThat(result.getContent().size()).isEqualTo(2);
        assertThat(result.getContent().get(0).name()).isEqualTo("소고기");
    }

    /// ///예외 테스트
    @Test
    @DisplayName("상품 조회 실패 시 예외 발생")
    void getProduct_notFound_throwsException() {
        // given
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> productService.getProduct(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ProductService.NOT_EXIST_PRODUCT);
    }
}