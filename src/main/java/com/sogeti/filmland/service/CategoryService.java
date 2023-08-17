package com.sogeti.filmland.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.sogeti.filmland.exception.BadRequestException;
import com.sogeti.filmland.model.DTO.CategoryDTO;
import com.sogeti.filmland.model.DTO.SubscribedCategoryDTO;
import com.sogeti.filmland.model.entity.Category;
import com.sogeti.filmland.model.response.CategoryResponse;
import com.sogeti.filmland.repository.CategoryRepository;
import com.sogeti.filmland.repository.CustomerRepository;
import com.sogeti.filmland.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
	private final CategoryRepository categoryRepository;
	private final SubscriptionRepository subscriptionRepository;
	private final CustomerRepository customerRepository;
	private final ModelMapper modelMapper;

	public List<CategoryDTO> getAllCategories() {
		List<Category> categories = categoryRepository.findAll();
		log.info("Getting all categories...");
		return categories.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public CategoryDTO convertToDTO(Category category) {
		return modelMapper.map(category, CategoryDTO.class);
	}

	public CategoryResponse getAvailableAndSubscribedCategories(String customerName) {

		return Optional.ofNullable(customerRepository.findByName(customerName)).map(c -> {
			List<CategoryDTO> availableCategoryDTOs = categoryRepository.findAvailableCategoriesForCustomer(c.getId())
					.stream().map(category -> new CategoryDTO(category.getId(), category.getName(),
							category.getAvailableContent(), category.getPrice()))
					.collect(Collectors.toList());

			List<SubscribedCategoryDTO> subscribedCategoryDTOs = subscriptionRepository
					.findSubscribedCategoriesForCustomer(c.getId()).stream()
					.map(subscription -> new SubscribedCategoryDTO(subscription.getName(),
							subscription.getRemainingContent(), subscription.getPrice(), subscription.getStartDate()))
					.collect(Collectors.toList());

			CategoryResponse categoryResponse = new CategoryResponse();
			categoryResponse.setAvailableCategories(availableCategoryDTOs);
			categoryResponse.setSubscribedCategories(subscribedCategoryDTOs);
			log.info("Getting available and subscribed categories for customer: {}", customerName);

			return categoryResponse;
		}).orElseThrow(() -> new BadRequestException("Oops ! You are not subscribed to FilmLand ! Please Signup ! "));
	}

}
