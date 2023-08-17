package com.sogeti.filmland.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sogeti.filmland.model.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	Optional<Customer> findByEmail(String email);

	Customer findByName(String name);

}