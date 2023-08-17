package com.sogeti.filmland.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sogeti.filmland.model.response.CategoryResponse;
import com.sogeti.filmland.service.CategoryService;

class CategoryControllerTest {

    private CategoryController categoryController;
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = mock(CategoryService.class);
        categoryController = new CategoryController(categoryService);
    }

    @Test
    void testGetCategoriesForCustomer() {
        String customerName = "testCustomer";
        CategoryResponse categoryResponse = new CategoryResponse();

        when(categoryService.getAvailableAndSubscribedCategories(customerName))
            .thenReturn(categoryResponse);

        ResponseEntity<CategoryResponse> responseEntity = categoryController.getCategoriesForCustomer(customerName);

        verify(categoryService).getAvailableAndSubscribedCategories(customerName);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(categoryResponse, responseEntity.getBody());
    }
}
