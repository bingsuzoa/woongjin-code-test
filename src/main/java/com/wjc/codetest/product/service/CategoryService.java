package com.wjc.codetest.product.service;

import com.wjc.codetest.global.exception.BusinessException;
import com.wjc.codetest.global.exception.ErrorCode;
import com.wjc.codetest.product.controller.dto.request.category.CreateCategoryRequest;
import com.wjc.codetest.product.controller.dto.response.category.CategoryDto;
import com.wjc.codetest.product.model.domain.category.Category;
import com.wjc.codetest.product.model.domain.category.CategoryName;
import com.wjc.codetest.product.model.domain.product.Product;
import com.wjc.codetest.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.wjc.codetest.global.exception.ErrorCode.CATEGORY_ERROR_002;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryDto create(CreateCategoryRequest createCategoryRequest) {
        if (categoryRepository.existsByName(createCategoryRequest.name())) {
            throw new BusinessException(ErrorCode.CATEGORY_ERROR_001);
        }
        CategoryName categoryName = CategoryName.from(createCategoryRequest.name());
        Category category = categoryRepository.save(new Category(categoryName));
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
