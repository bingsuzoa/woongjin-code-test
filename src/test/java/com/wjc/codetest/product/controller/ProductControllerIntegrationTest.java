package com.wjc.codetest.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wjc.codetest.product.controller.dto.request.product.CreateProductRequest;
import com.wjc.codetest.product.controller.dto.request.product.UpdateProductRequest;
import com.wjc.codetest.product.model.domain.category.Category;
import com.wjc.codetest.product.model.domain.category.CategoryName;
import com.wjc.codetest.product.model.domain.product.Product;
import com.wjc.codetest.product.model.domain.product.ProductCode;
import com.wjc.codetest.product.model.domain.product.ProductName;
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

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

    /// ///해피 테스트
    @BeforeEach
    void setUp() {
        category = categoryRepository.save(new Category(CategoryName.from("식품")));
    }

    @Test
    @DisplayName("상품 생성 시 201 Created와 생성된 상품 반환")
    void createProduct_success() throws Exception {
        // given
        CreateProductRequest request = new CreateProductRequest(
                category.getId(),
                "초코파이",
                "PRD-AAA111"
        );

        // when & then
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("초코파이"))
                .andExpect(jsonPath("$.categoryId").value(category.getId()));
    }

    @Test
    @DisplayName("상품 단건 조회 - 200 OK")
    void getProduct_success() throws Exception {
        // given
        Product saved = productRepository.save(
                new Product(category, ProductName.from("라면"), ProductCode.from("PRD-BBB222"))
        );

        // when & then
        mockMvc.perform(get("/products/{productId}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("라면"));
    }

    @Test
    @DisplayName("상품 수정 시 200 OK와 업데이트된 결과 반환")
    void updateProduct_success() throws Exception {
        // given
        Product saved = productRepository.save(
                new Product(category, ProductName.from("라면"), ProductCode.from("PRD-CCC333"))
        );
        UpdateProductRequest request = new UpdateProductRequest(
                saved.getId(),
                category.getId(),
                "신라면"
        );

        // when & then
        mockMvc.perform(put("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("신라면"));
    }

    @Test
    @DisplayName("상품 삭제 시 200 OK 반환 및 데이터 삭제 확인")
    void deleteProduct_success() throws Exception {
        // given
        Product saved = productRepository.save(
                new Product(category, ProductName.from("과자"), ProductCode.from("PRD-DDD444"))
        );

        // when & then
        mockMvc.perform(delete("/products/{productId}", saved.getId()))
                .andExpect(status().isNoContent());

        // verify
        assertThat(productRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    @DisplayName("카테고리별 상품 페이징 조회 - 200 OK")
    void getProductsOfCategory_success() throws Exception {
        // given
        IntStream.rangeClosed(1, 15)
                .forEach(i -> productRepository.save(
                        new Product(category,
                                ProductName.from("상품" + i),
                                ProductCode.from("PRD-AAA" + (100 + i)))
                ));

        // when & then
        mockMvc.perform(get("/products/category/{categoryId}", category.getId())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").isArray())
                .andExpect(jsonPath("$.totalElements").value(15))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.page").value(0));
    }

    /// ///예외 테스트
    @Test
    @DisplayName("상품 생성 실패 - name이 null이면 VALIDATION_ERROR 반환")
    void createProduct_nullName_returnsValidationError() throws Exception {
        // given
        String invalidRequest = """
            {
              "categoryId": 1,
              "name": null,
              "productCode": "PRD-AAA111"
            }
        """;

        // when & then
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("상품 생성 실패 - categoryId가 null이면 VALIDATION_ERROR 반환")
    void createProduct_nullCategory_returnsValidationError() throws Exception {
        // given
        String invalidRequest = """
            {
              "categoryId": null,
              "name": "초코파이",
              "productCode": "PRD-AAA111"
            }
        """;

        // when & then
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("상품 생성 실패 - productCode가 null이면 VALIDATION_ERROR 반환")
    void createProduct_nullProductCode_returnsValidationError() throws Exception {
        // given
        String invalidRequest = """
            {
              "categoryId": 1,
              "name": "초코파이",
              "productCode": null
            }
        """;

        // when & then
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("VALIDATION_ERROR"));
    }
}