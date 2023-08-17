package com.sogeti.filmland.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sogeti.filmland.model.DTO.CategoryDTO;
import com.sogeti.filmland.model.response.CategoryResponse;
import com.sogeti.filmland.service.CategoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/sogeti/filmland")
@Slf4j
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	@GetMapping("/categories")
	public ResponseEntity<List<CategoryDTO>> getCategories() {
		log.info("Request received to get all Categories");

		List<CategoryDTO> categories = categoryService.getAllCategories();
		return ResponseEntity.ok(categories);
	}

	@GetMapping("/customer/{customerName}")
	public ResponseEntity<CategoryResponse> getCategoriesForCustomer(@PathVariable String customerName) {
		log.info("Request received to check all available and Subscribed Categories for " + customerName);
		CategoryResponse categories = categoryService.getAvailableAndSubscribedCategories(customerName);
		return ResponseEntity.ok(categories);
	}
}
