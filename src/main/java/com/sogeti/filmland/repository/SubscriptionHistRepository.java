package com.sogeti.filmland.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sogeti.filmland.model.entity.SubscriptionHistory;

@Repository
public interface SubscriptionHistRepository extends JpaRepository<SubscriptionHistory, Long> {


	SubscriptionHistory save(SubscriptionHistory s);
	
	long countByCustomerId(Long customerId);

}