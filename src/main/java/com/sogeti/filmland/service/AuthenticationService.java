package com.sogeti.filmland.service;

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sogeti.filmland.configuration.JwtService;
import com.sogeti.filmland.model.entity.Customer;
import com.sogeti.filmland.model.entity.Token;
import com.sogeti.filmland.model.enums.TokenType;
import com.sogeti.filmland.model.request.AuthenticationRequest;
import com.sogeti.filmland.model.request.RegisterRequest;
import com.sogeti.filmland.model.response.AuthenticationResponse;
import com.sogeti.filmland.repository.CustomerRepository;
import com.sogeti.filmland.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final CustomerRepository repository;
	private final TokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public AuthenticationResponse register(RegisterRequest request) {
		var user = Customer.builder().name(request.getFirstname())
				.email(request.getEmail()).password(passwordEncoder.encode(request.getPassword()))
				.build();
		var savedUser = repository.save(user);
		var jwtToken = jwtService.generateToken(user);
		saveUserToken(savedUser, jwtToken);
		return AuthenticationResponse.builder().accessToken(jwtToken).status("Registration successful")
				.message("You are set to subscribe to your happiness with FilmLand !!").build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		var user = repository.findByEmail(request.getEmail()).orElseThrow();
		var jwtToken = jwtService.generateToken(user);
		revokeAllUserTokens(user);
		saveUserToken(user, jwtToken);
		return AuthenticationResponse.builder().accessToken(jwtToken).status("Login successful")
				.message("Enjoy Your Time With FilmLand").build();
	}

	private void saveUserToken(Customer customer, String jwtToken) {
		var token = Token.builder().customer(customer).token(jwtToken).tokenType(TokenType.BEARER).expired(false)
				.revoked(false).build();
		tokenRepository.save(token);
	}

	private void revokeAllUserTokens(Customer customer) {
		var validUserTokens = tokenRepository.findAllValidTokenByCustomer(customer.getId());
		if (validUserTokens.isEmpty())
			return;
		validUserTokens.forEach(token -> {
			token.setExpired(true);
			token.setRevoked(true);
		});
		tokenRepository.saveAll(validUserTokens);
	}

	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String refreshToken;
		final String userEmail;
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return;
		}
		refreshToken = authHeader.substring(7);
		userEmail = jwtService.extractUsername(refreshToken);
		if (userEmail != null) {
			var user = this.repository.findByEmail(userEmail).orElseThrow();
			if (jwtService.isTokenValid(refreshToken, user)) {
				var accessToken = jwtService.generateToken(user);
				revokeAllUserTokens(user);
				saveUserToken(user, accessToken);
				var authResponse = AuthenticationResponse.builder().accessToken(accessToken).build();
				new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
			}
		}
	}
}