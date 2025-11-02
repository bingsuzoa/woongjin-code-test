package com.wjc.codetest.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.wjc.codetest.product.controller.dto.request.category.CreateCategoryRequest;
import com.wjc.codetest.product.controller.dto.response.category.CategoryDto;
import com.wjc.codetest.product.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @Test
    @DisplayName("카테고리 생성 성공 - 201 상태와 CategoryDto 반환")
    void createCategory_success() throws Exception {
        // given
        CreateCategoryRequest request = new CreateCategoryRequest("육류");
        CategoryDto response = new CategoryDto(1L, "육류");

        given(categoryService.create(any(CreateCategoryRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("육류"));
    }

    @Test
    @DisplayName("등록된 상품들의 카테고리 목록 조회 - 200 OK")
    void getCategoriesOfRegisteredProducts_success() throws Exception {
        // given
        List<CategoryDto> categories = List.of(
                new CategoryDto(1L, "육류"),
                new CategoryDto(2L, "과일")
        );
        given(categoryService.getCategoriesOfRegisteredProducts()).willReturn(categories);

        // when & then
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("육류"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("과일"));
    }
}
