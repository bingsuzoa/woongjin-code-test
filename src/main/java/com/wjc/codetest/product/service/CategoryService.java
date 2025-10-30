package com.wjc.codetest.product.service;

import com.wjc.codetest.product.controller.dto.request.category.CreateCategoryRequest;
import com.wjc.codetest.product.controller.dto.response.category.CategoryDto;
import com.wjc.codetest.product.model.domain.Category;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public static final String NOT_EXIST_CATEGORY = "category not found";

    public CategoryDto create(CreateCategoryRequest createCategoryRequest) {
        Category category = categoryRepository.save(new Category(createCategoryRequest.name()));
        return new CategoryDto(category.getId(), category.getName());
    }

    public Category updateCategory(Product product, Long categoryId) {
        if (!product.isInCategory(categoryId)) {
            Category newCategory = getCategoryEntity(categoryId);
            return product.changeCategory(newCategory);
        }
        return getCategoryEntity(categoryId);
    }

    public Category getCategoryEntity(Long categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isEmpty()) {
            throw new IllegalArgumentException(NOT_EXIST_CATEGORY);
        }
        return categoryOptional.get();
    }

    public List<CategoryDto> getCategoriesOfRegisteredProducts() {
        List<Category> categories = categoryRepository.getCategoriesOfRegisteredProducts();
        return categories.stream()
                .map(category -> new CategoryDto(category.getId(), category.getName()))
                .toList();
    }
}
