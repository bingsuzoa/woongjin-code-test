package com.wjc.codetest.product.service;

import com.wjc.codetest.global.exception.BusinessException;
import com.wjc.codetest.global.response.ResponseCode;
import com.wjc.codetest.product.controller.dto.request.category.CreateCategoryRequest;
import com.wjc.codetest.product.controller.dto.response.category.CategoryDto;
import com.wjc.codetest.product.model.domain.Category;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.wjc.codetest.global.response.ResponseCode.CATEGORY_ERROR_002;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryDto create(CreateCategoryRequest createCategoryRequest) {
        if (categoryRepository.existsByName(createCategoryRequest.name())) {
            throw new BusinessException(ResponseCode.CATEGORY_ERROR_001);
        }
        Category category = categoryRepository.save(new Category(createCategoryRequest.name()));
        return new CategoryDto(category.getId(), category.getName());
    }

    @Transactional
    public Category updateCategory(Product product, Long categoryId) {
        if (!product.isInCategory(categoryId)) {
            Category newCategory = getCategoryEntity(categoryId);
            return product.changeCategory(newCategory);
        }
        return getCategoryEntity(categoryId);
    }

    @Transactional(readOnly = true)
    public Category getCategoryEntity(Long categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isEmpty()) {
            throw new BusinessException(CATEGORY_ERROR_002);
        }
        return categoryOptional.get();
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getCategoriesOfRegisteredProducts() {
        return categoryRepository.findAllWithProductsDto();
    }
}
