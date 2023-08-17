package com.sogeti.filmland.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

	@NotBlank(message = "{email.missing}")
	@Email(message = "{email.invalid}")
	private String email;
	@NotBlank(message = "{password.missing}")
	String password;
}