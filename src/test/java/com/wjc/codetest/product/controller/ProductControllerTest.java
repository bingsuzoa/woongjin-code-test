package com.wjc.codetest.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wjc.codetest.product.controller.dto.request.product.CreateProductRequest;
import com.wjc.codetest.product.controller.dto.request.product.UpdateProductRequest;
import com.wjc.codetest.product.controller.dto.response.product.ProductDto;
import com.wjc.codetest.product.service.ProductService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // JSON 직렬화/역직렬화용

    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("상품 단건 조회 - 성공")
    void getProduct_success() throws Exception {
        // given
        ProductDto dto = new ProductDto(1L, 10L, "삼겹살", "productCode1");
        given(productService.getProduct(1L)).willReturn(dto);

        // when & then
        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("삼겹살"));
    }

    @Test
    @DisplayName("상품 생성 - 201 Created 반환")
    void createProduct_success() throws Exception {
        // given
        CreateProductRequest request = new CreateProductRequest(10L, "돼지고기", "productCode1");
        ProductDto response = new ProductDto(1L, 10L, "돼지고기", "productCode1");
        given(productService.createProduct(any(CreateProductRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("돼지고기"))
                .andExpect(jsonPath("$.productCode").isNotEmpty());
    }

    @Test
    @DisplayName("상품 삭제 - 성공 시 200 OK 반환")
    void deleteProduct_success() throws Exception {
        // given
        Mockito.doNothing().when(productService).delete(1L);

        // when & then
        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("상품 전체 수정(PUT) - 성공")
    void updateProduct_success() throws Exception {
        // given
        UpdateProductRequest request = new UpdateProductRequest(1L, 10L, "양념갈비");
        ProductDto response = new ProductDto(1L, 10L, "양념갈비", "productCode1");
        given(productService.update(any(UpdateProductRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(put("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("양념갈비"));
    }

    @Test
    @DisplayName("카테고리별 상품 조회 - 페이징 결과 반환")
    void getProductsOfCategory_success() throws Exception {
        // given
        ProductDto p1 = new ProductDto(1L, 10L, "삼겹살", "productCode1");
        ProductDto p2 = new ProductDto(2L, 10L, "목살", "productCode2");
        Page<ProductDto> page = new PageImpl<>(List.of(p1, p2));
        given(productService.getProductsByCategory(eq(10L), eq(0), eq(10))).willReturn(page);

        // when & then
        mockMvc.perform(get("/products/category/10")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").isArray())
                .andExpect(jsonPath("$.products[0].name").value("삼겹살"))
                .andExpect(jsonPath("$.products[1].name").value("목살"));
    }
}