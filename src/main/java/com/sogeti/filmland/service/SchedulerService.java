package com.sogeti.filmland.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;
import com.sogeti.filmland.model.entity.Subscription;
import com.sogeti.filmland.model.entity.SubscriptionHistory;
import com.sogeti.filmland.repository.SubscriptionHistRepository;
import com.sogeti.filmland.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {

	private final SubscriptionRepository subscriptionRepository;
	private final SubscriptionHistRepository subscriptionHistRepository;

	public void processMonthlyPayments() {

		LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
		List<Subscription> subscriptions = subscriptionRepository.findByStartDateBefore(oneMonthAgo);
		for (Subscription subscription : subscriptions) {

			SubscriptionHistory s = new SubscriptionHistory();
			s.setCategory(subscription.getCategory());
			s.setCustomer(subscription.getCustomer());
			s.setName(subscription.getCategory().getName());
			s.setPrice(subscription.getPrice());
			s.setRemainingContent(subscription.getRemainingContent());
			s.setStartDate(subscription.getStartDate());
			subscriptionHistRepository.save(s);
			subscriptionRepository.delete(subscription);
			//TODO : Send Email notification 
			log.info("Dear " + subscription.getCustomer().getName()
					+ ",Please subscribe to the service for further use");

		}
	}

}