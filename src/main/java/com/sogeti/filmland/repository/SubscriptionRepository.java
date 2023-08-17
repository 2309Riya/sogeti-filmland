package com.sogeti.filmland.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.sogeti.filmland.model.entity.Subscription;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

	Subscription findByName(String name);

	@Query("SELECT s FROM Subscription s WHERE s.customer.id = :id")
	List<Subscription> findSubscribedCategoriesForCustomer(@Param("id") long id);

	@Query("SELECT s FROM Subscription s WHERE s.customer.id = :id and category.id =:catId")
	Subscription findSubscribedCategoryForCustomer(@Param("id") long id, @Param("catId") long catId);

	List<Subscription> findByStartDateBefore(LocalDate date);

	long countByCustomerId(Long customerId);

	void delete(Subscription existingSubscription);
}