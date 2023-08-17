package com.sogeti.filmland;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableJpaRepositories
@ComponentScan(basePackages = "com.sogeti.filmland")
@Slf4j
public class FilmLandApplication {

	public static void main(String[] args) {
		log.info("FilmLand Application Started");
		SpringApplication.run(FilmLandApplication.class, args);
}
}