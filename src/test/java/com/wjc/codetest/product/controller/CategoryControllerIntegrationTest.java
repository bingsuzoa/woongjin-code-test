package com.wjc.codetest.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wjc.codetest.product.controller.dto.request.category.CreateCategoryRequest;
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
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("카테고리 생성 성공 - 200 OK 및 CategoryDto 반환")
    void createCategory_success() throws Exception {
        // given
        CreateCategoryRequest request = new CreateCategoryRequest("식품");

        // when & then
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("식품"));

        // verify
        assertThat(categoryRepository.findAll()).hasSize(1);
        assertThat(categoryRepository.findAll().get(0).getName()).isEqualTo("식품");
    }

    @Test
    @DisplayName("상품이 등록된 카테고리만 조회된다")
    void getCategoriesOfRegisteredProducts_success() throws Exception {
        Category category1 = categoryRepository.save(new Category("식품"));
        Category category2 = categoryRepository.save(new Category("가전"));
        Category category3 = categoryRepository.save(new Category("패션"));

        productRepository.save(new Product(category1, "초코파이"));
        productRepository.save(new Product(category2, "노트북"));

        // when & then
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
