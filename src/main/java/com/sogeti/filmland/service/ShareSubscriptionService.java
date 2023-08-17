package com.sogeti.filmland.service;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.sogeti.filmland.exception.BadRequestException;
import com.sogeti.filmland.exception.CustomerNotFoundException;
import com.sogeti.filmland.exception.SubscriptionException;
import com.sogeti.filmland.model.entity.Category;
import com.sogeti.filmland.model.entity.Customer;
import com.sogeti.filmland.model.entity.Subscription;
import com.sogeti.filmland.model.enums.SubscriptionType;
import com.sogeti.filmland.model.request.ShareSubscriptionRequest;
import com.sogeti.filmland.model.response.ResponseDTO;
import com.sogeti.filmland.repository.CategoryRepository;
import com.sogeti.filmland.repository.CustomerRepository;
import com.sogeti.filmland.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShareSubscriptionService {

	private final CustomerRepository customerRepository;
	private final CategoryRepository categoryRepository;
	private final SubscriptionRepository subscriptionRepository;

	public ResponseDTO shareSubscription(ShareSubscriptionRequest request) {

		Customer existingCustomer = Optional.ofNullable(customerRepository.findByEmail(request.getEmail())).get()
				.orElseThrow(() -> new CustomerNotFoundException());
		Customer sharedCustomer = Optional.ofNullable(customerRepository.findByEmail(request.getCustomerEmail())).get()
				.orElseThrow(() -> new CustomerNotFoundException());

		Category category = Optional.ofNullable(categoryRepository.findByName(request.getSubscribedCategory()))
				.orElseThrow(() -> new SubscriptionException());

		Subscription subscribedCategory = Optional
				.ofNullable(subscriptionRepository.findSubscribedCategoryForCustomer(existingCustomer.getId(),
						category.getId()))
				.orElseThrow(() -> new BadRequestException(
						"Not subscribed to this category ! Plese subscribe before Sharing !! "));

		if (subscribedCategory.getPrice() == 0) {
			throw new BadRequestException("Sorry Free Subscription can not be shared !!" + category.getName()
					+ " is Free Subscribtion only for yo!");
		}
		if (subscribedCategory.getRemainingContent() == 1) {
			throw new BadRequestException("Sorry you have only 1 content left for !!" + category.getName()
					+ " category ! You can not share this category! ");
		}

		Optional.ofNullable(
				subscriptionRepository.findSubscribedCategoryForCustomer(sharedCustomer.getId(), category.getId()))
				.ifPresent(s -> {
					throw new BadRequestException("Already Subscribed to " + s.getName() + " !!");
				});
		double pricePerContent = calculateSharedPriceForCategory(subscribedCategory.getCategory().getAvailableContent(),
				subscribedCategory.getRemainingContent(), subscribedCategory.getCategory().getPrice());

		int remainingContent = subscribedCategory.getRemainingContent() / 2;
		if (subscribedCategory.getRemainingContent() % 2 == 0) {
			subscribedCategory.setRemainingContent(remainingContent);
			subscribedCategory.setPrice(pricePerContent * (remainingContent));
		}

		else {
			subscribedCategory.setRemainingContent(remainingContent + 1);
			subscribedCategory.setPrice(pricePerContent * (remainingContent + 1));

		}

		subscriptionRepository.save(subscribedCategory);
		System.out.println("saved " + subscribedCategory.toString());
		subscriptionRepository.save(subscribedCategory);

		subscriptionRepository.save(Subscription.builder().customer(sharedCustomer).category(category)
				.name(category.getName()).price(pricePerContent * remainingContent).remainingContent(remainingContent)
				.startDate(LocalDate.now()).subsType(SubscriptionType.SHARED).build());

		log.info("Subscription is shared");

		return new ResponseDTO("Subscription shared successfully",
				"Subscription for category '" + request.getSubscribedCategory() + "' has been shared.");

	}

	private double calculateSharedPriceForCategory(int availableContent, int remainingContent, double price) {

		double pricePerContent = price / availableContent;
		return pricePerContent;

	}
}
