package com.sogeti.filmland.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShareSubscriptionRequest {

	@NotBlank(message = "{email.missing}")
	@Email(message = "{email.invalid}")
	private String email;

	@NotBlank(message = "{email.missing}")
	@Email(message = "{email.invalid}")
	private String customerEmail;

	@NotBlank(message = "{email.invalid}")
	private String subscribedCategory;

}