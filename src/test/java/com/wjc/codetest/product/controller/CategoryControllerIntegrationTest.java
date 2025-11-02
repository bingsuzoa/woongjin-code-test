package com.wjc.codetest.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wjc.codetest.product.controller.dto.request.category.CreateCategoryRequest;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class CategoryControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    /// ///해피 테스트

    @Test
    @DisplayName("카테고리 생성 성공 - 201 및 CategoryDto 반환")
    void createCategory_success() throws Exception {
        // given
        CreateCategoryRequest request = new CreateCategoryRequest("식품");

        // when & then
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("식품"));

        // verify
        List<Category> categories = categoryRepository.findAll();
        assertThat(categories).hasSize(1);
        assertThat(categories.get(0).getName()).isEqualTo("식품");
    }

    @Test
    @DisplayName("상품이 등록된 카테고리만 조회된다 - 200 및 배열 반환")
    void getCategoriesOfRegisteredProducts_success() throws Exception {
        // given
        Category category1 = categoryRepository.save(new Category(CategoryName.from("식품")));
        Category category2 = categoryRepository.save(new Category(CategoryName.from("가전")));
        Category category3 = categoryRepository.save(new Category(CategoryName.from("패션")));

        productRepository.save(new Product(category1, ProductName.from("초코파이"), ProductCode.from("PRD-251001")));
        productRepository.save(new Product(category2, ProductName.from("노트북"), ProductCode.from("PRD-252002")));

        // when & then
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").isNotEmpty());
    }
    /// ///예외 테스트
    @Test
    @DisplayName("카테고리 생성 실패 - name이 null이면 VALIDATION_ERROR 반환")
    void createCategory_validationFail_returnsValidationError() throws Exception {
        // given
        String invalidRequest = """
            { "name": null }
        """;

        // when & then
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("VALIDATION_ERROR"));
    }
}
