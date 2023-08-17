package com.sogeti.filmland.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionRequest {

	@NotBlank(message = "{email.missing}")
	@Email(message = "{email.invalid}")
	private String email;

	@NotBlank(message = "{category.missing}")
	private String availableCategory;

}