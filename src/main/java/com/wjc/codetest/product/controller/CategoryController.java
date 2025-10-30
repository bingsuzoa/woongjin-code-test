package com.wjc.codetest.product.controller;

import com.wjc.codetest.product.controller.dto.request.category.CreateCategoryRequest;
import com.wjc.codetest.product.controller.dto.response.category.CategoryDto;
import com.wjc.codetest.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CreateCategoryRequest createCategoryRequest) {
        return ResponseEntity.ok(categoryService.create(createCategoryRequest));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategoriesOfRegisteredProducts(){
        return ResponseEntity.ok(categoryService.getCategoriesOfRegisteredProducts());
    }
}
