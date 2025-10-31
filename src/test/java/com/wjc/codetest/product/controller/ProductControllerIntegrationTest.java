package com.wjc.codetest.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wjc.codetest.product.controller.dto.request.product.CreateProductRequest;
import com.wjc.codetest.product.controller.dto.request.product.UpdateProductRequest;
import com.wjc.codetest.product.model.domain.Category;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.repository.CategoryRepository;
import com.wjc.codetest.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class ProductControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired private ProductRepository productRepository;
    @Autowired private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
     void setUp() {
        category = categoryRepository.save(new Category("식품"));
    }

    @Test
    @DisplayName("상품 생성 시 201 Created와 Location 헤더를 반환한다")
    void createProduct_success() throws Exception {
        CreateProductRequest request = new CreateProductRequest(category.getId(), "초코파이", "productCode1");

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/products/")))
                .andExpect(jsonPath("$.name").value("초코파이"))
                .andExpect(jsonPath("$.categoryId").value(category.getId()));
    }

    @Test
    @DisplayName("상품 단건 조회 - 200 OK")
    void getProduct_success() throws Exception {
        Product saved = productRepository.save(new Product(category, "라면", "productCode1"));

        mockMvc.perform(get("/products/{productId}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("라면"))
                .andExpect(jsonPath("$.id").value(saved.getId()));
    }

    @Test
    @DisplayName("상품 수정 시 200 OK로 업데이트된 결과 반환")
    void updateProduct_success() throws Exception {
        Product saved = productRepository.save(new Product(category, "라면", "productCode1"));
        UpdateProductRequest request = new UpdateProductRequest(saved.getId(), category.getId(), "신라면");

        mockMvc.perform(put("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("신라면"));
    }

    @Test
    @DisplayName("상품 삭제 시 true와 200 OK 반환")
    void deleteProduct_success() throws Exception {
        Product saved = productRepository.save(new Product(category, "과자", "productCode1"));

        mockMvc.perform(delete("/products/{productId}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

        assertThat(productRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    @DisplayName("카테고리별 상품 페이징 조회")
    void getProductsOfCategory_success() throws Exception {
        IntStream.rangeClosed(1, 15)
                .forEach(i -> productRepository.save(new Product(category, "상품" + i, "productCode" + i)));

        mockMvc.perform(get("/products/category/{categoryId}", category.getId())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").isArray())
                .andExpect(jsonPath("$.totalElements").value(15))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.page").value(0));
    }
}