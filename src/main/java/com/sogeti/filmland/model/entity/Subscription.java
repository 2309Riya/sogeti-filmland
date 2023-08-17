package com.sogeti.filmland.model.entity;

import java.time.LocalDate;

import com.sogeti.filmland.model.enums.SubscriptionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subscription")
public class Subscription {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@Column(name = "name")
	private String name;
	@Column(name = "remainingContent")
	private int remainingContent;
	private double price;
	private LocalDate startDate;
	private SubscriptionType subsType = SubscriptionType.PAID;

}
