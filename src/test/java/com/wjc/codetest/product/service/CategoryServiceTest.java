package com.wjc.codetest.product.service;

import com.wjc.codetest.product.controller.dto.request.category.CreateCategoryRequest;
import com.wjc.codetest.product.controller.dto.response.category.CategoryDto;
import com.wjc.codetest.product.model.domain.Category;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryService categoryService;

    private static final String CATEGORY_NAME = "육류";

    /// ///해피 테스트
    @Test
    @DisplayName("카테고리 생성 시 DTO로 반환된다")
    void createCategory_success() {
        // given
        CreateCategoryRequest request = new CreateCategoryRequest(CATEGORY_NAME);
        Category saved = new Category(CATEGORY_NAME);
        ReflectionTestUtils.setField(saved, "id", 1L);

        when(categoryRepository.save(any())).thenReturn(saved);

        // when
        CategoryDto result = categoryService.create(request);

        // then
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo(CATEGORY_NAME);
    }

    @Test
    @DisplayName("카테고리 조회 성공 시 Category 엔티티 반환")
    void getCategoryEntity_success() {
        // given
        Category category = new Category(CATEGORY_NAME);
        ReflectionTestUtils.setField(category, "id", 1L);

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        // when
        Category result = categoryService.getCategoryEntity(1L);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo(CATEGORY_NAME);
    }

    @Test
    @DisplayName("상품이 다른 카테고리에 속할 경우 변경 후 새 Category 반환")
    void updateCategory_changeCategory_success() {
        // given
        Product product = mock(Product.class);
        Category newCategory = new Category("과일");
        ReflectionTestUtils.setField(newCategory, "id", 2L);

        when(product.isInCategory(anyLong())).thenReturn(false);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(newCategory));
        when(product.changeCategory(any(Category.class))).thenReturn(newCategory);

        // when
        Category result = categoryService.updateCategory(product, 2L);

        // then
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getName()).isEqualTo("과일");
        verify(product).changeCategory(any(Category.class));
    }

    @Test
    @DisplayName("상품이 이미 해당 카테고리에 속할 경우 변경하지 않는다")
    void updateCategory_alreadyInCategory() {
        // given
        Product product = mock(Product.class);
        Category sameCategory = new Category(CATEGORY_NAME);
        ReflectionTestUtils.setField(sameCategory, "id", 1L);

        when(product.isInCategory(1L)).thenReturn(true);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(sameCategory));

        // when
        Category result = categoryService.updateCategory(product, 1L);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo(CATEGORY_NAME);
    }

    @Test
    @DisplayName("등록된 상품이 있는 카테고리 목록 조회 성공")
    void getCategoriesOfRegisteredProducts_success() {
        // given
        CategoryDto category1 = new CategoryDto(1L, "육류");
        CategoryDto category2 = new CategoryDto(2L, "과일");
        CategoryDto category3 = new CategoryDto(3L, "가전");

        when(categoryRepository.findAllWithProductsDto())
                .thenReturn(List.of(category1, category2));

        // when
        List<CategoryDto> result = categoryService.getCategoriesOfRegisteredProducts();

        // then
        assertThat(result.size()).isEqualTo(2);
    }


    /// ///예외 테스트
    @Test
    @DisplayName("존재하지 않는 카테고리 조회 시 예외 발생")
    void getCategoryEntity_notFound_throwsException() {
        // given
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> categoryService.getCategoryEntity(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CategoryService.NOT_EXIST_CATEGORY);
    }
}
