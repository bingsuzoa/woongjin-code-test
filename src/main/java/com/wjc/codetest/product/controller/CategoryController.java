package com.wjc.codetest.product.controller;

import com.wjc.codetest.global.response.ApiResponse;
import com.wjc.codetest.global.response.ResponseCode;
import com.wjc.codetest.product.controller.dto.request.category.CreateCategoryRequest;
import com.wjc.codetest.product.controller.dto.response.category.CategoryDto;
import com.wjc.codetest.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryDto>> createCategory(@RequestBody CreateCategoryRequest createCategoryRequest) {
        CategoryDto category = categoryService.create(createCategoryRequest);
        return ApiResponse.success(ResponseCode.CATEGORY_SUCCESS, HttpStatus.CREATED, category);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getCategoriesOfRegisteredProducts() {
        List<CategoryDto> categories = categoryService.getCategoriesOfRegisteredProducts();
        return ApiResponse.success(ResponseCode.CATEGORY_SUCCESS, HttpStatus.OK, categories);
    }
}
