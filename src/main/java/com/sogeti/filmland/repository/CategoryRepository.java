package com.sogeti.filmland.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.sogeti.filmland.model.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	
	Category findByName(String name);
	
    @Query("SELECT c FROM Category c WHERE c.id NOT IN (SELECT s.category.id FROM Subscription s WHERE s.customer.id = :id)")
    List<Category> findAvailableCategoriesForCustomer(@Param("id") long id);

}