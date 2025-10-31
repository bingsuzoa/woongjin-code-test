package com.wjc.codetest.product.controller;

import com.wjc.codetest.global.response.UriBuilder;
import com.wjc.codetest.product.controller.dto.request.category.CreateCategoryRequest;
import com.wjc.codetest.product.controller.dto.response.category.CategoryDto;
import com.wjc.codetest.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CreateCategoryRequest createCategoryRequest) {
        CategoryDto category = categoryService.create(createCategoryRequest);
        return ResponseEntity.created(UriBuilder.buildCurrentUri(category.id())).body(category);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategoriesOfRegisteredProducts(){
        return ResponseEntity.ok(categoryService.getCategoriesOfRegisteredProducts());
    }
}
