package com.sogeti.filmland.configuration;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

	private final JwtAuthenticationFilter jwtAuthFilter;
	private final AuthenticationProvider authenticationProvider;
	private final LogoutHandler logoutHandler;

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity http) throws Exception {
		http.securityMatcher(PathRequest.toH2Console());
		http.authorizeHttpRequests(
				auth -> auth.requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll());
		http.csrf((csrf) -> csrf.disable());
		http.headers((headers) -> headers.frameOptions((frame) -> frame.sameOrigin()));
		return http.build();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector)
			throws Exception {
		// ...
		MvcRequestMatcher h2RequestMatcher = new MvcRequestMatcher(introspector, "/**");
		h2RequestMatcher.setServletPath("/h2-console");

		http.csrf().disable().authorizeHttpRequests().requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
				.permitAll().requestMatchers(h2RequestMatcher).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/actuator")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/sogeti/filmland/auth/**")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/v2/api-docs")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/v3/api-docs")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("swagger-resources")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/configuration/security")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("webjars/**")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/swagger-ui.html")).permitAll().anyRequest().authenticated()
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).logout()
				.logoutUrl("/api/v1/auth/logout").addLogoutHandler(logoutHandler)
				.logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());

		return http.build();
	}

}
