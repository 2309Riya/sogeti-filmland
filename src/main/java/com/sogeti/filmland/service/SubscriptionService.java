package com.sogeti.filmland.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sogeti.filmland.exception.BadRequestException;
import com.sogeti.filmland.exception.CategoryNotFoundException;
import com.sogeti.filmland.exception.CustomerNotFoundException;
import com.sogeti.filmland.model.entity.Category;
import com.sogeti.filmland.model.entity.Customer;
import com.sogeti.filmland.model.entity.Subscription;
import com.sogeti.filmland.model.enums.SubscriptionType;
import com.sogeti.filmland.model.response.ResponseDTO;
import com.sogeti.filmland.repository.CategoryRepository;
import com.sogeti.filmland.repository.CustomerRepository;
import com.sogeti.filmland.repository.SubscriptionHistRepository;
import com.sogeti.filmland.repository.SubscriptionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {

	private final CustomerRepository customerRepository;
	private final CategoryRepository categoryRepository;
	private final SubscriptionRepository subscriptionRepository;
	private final SubscriptionHistRepository subscriptionHistRepository;

	@Transactional
	public ResponseDTO subscribeToCategory(String email, String categoryName) {

		Customer customer = Optional.ofNullable(customerRepository.findByEmail(email)).get()
				.orElseThrow(() -> new CustomerNotFoundException());

		Category category = Optional.ofNullable(categoryRepository.findByName(categoryName))
				.orElseThrow(() -> new CategoryNotFoundException());

		Optional<Subscription> existingSubscription = Optional.ofNullable(
				subscriptionRepository.findSubscribedCategoryForCustomer(customer.getId(), category.getId()));

		if (existingSubscription.isPresent()) {
			int remainingDay = calculateRemainingDays(existingSubscription.get().getStartDate());

			throw new BadRequestException(
					"You have already subscribed to this category. Remaining days for next subscription: "
							+ remainingDay);

		}
		if (customerHasNoSubscriptions(customer.getId())) {
			return provideFreeSubscription(customer, category);
		}

		subscriptionRepository.save(createSubscription(customer, category));
		log.info("Customer has subscribed successfully ");

		return new ResponseDTO("OK", "Wonderful You are susbscribed to " + categoryName + " !! Enjoy your time !");

	}

	private boolean customerHasNoSubscriptions(Long customerId) {

		if ((subscriptionHistRepository.countByCustomerId(customerId) == 0)
				&& ((subscriptionRepository.countByCustomerId(customerId) == 0)))
			return true;
		else if ((subscriptionRepository.countByCustomerId(customerId) == 1) && subscriptionRepository
				.findSubscribedCategoriesForCustomer(customerId).get(0).getSubsType() == SubscriptionType.SHARED)
			return true;

		return false;
	}

	private ResponseDTO provideFreeSubscription(Customer customer, Category category) {
		Subscription freeSubscription = Subscription.builder().customer(customer).category(category)
				.name(category.getName()).price(0.0) // Free subscription
				.remainingContent(category.getAvailableContent()).startDate(LocalDate.now())
				.subsType(SubscriptionType.FREE).build();

		subscriptionRepository.save(freeSubscription);

		return new ResponseDTO("OK",
				"Congratulations! You've been provided a free subscription to " + category.getName() + "!! Enjoy!");
	}

	private int calculateRemainingDays(LocalDate startDate) {
		LocalDate currentDate = LocalDate.now();
		Period period = Period.between(startDate, currentDate);
		int totalDaysInOneMonth = 30;
		int remainingDays = totalDaysInOneMonth - period.getDays();
		return Math.max(remainingDays, 0);
	}

	private Subscription createSubscription(Customer customer, Category category) {
		return Subscription.builder().customer(customer).category(category).name(category.getName())
				.price(category.getPrice()).remainingContent(category.getAvailableContent()).startDate(LocalDate.now())
				.subsType(SubscriptionType.PAID).build();
	}

}
