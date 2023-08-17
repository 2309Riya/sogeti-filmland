package com.sogeti.filmland.exception;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.sogeti.filmland.model.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final String CUSTOMER_NOT_FOUND = "Customer Not Found";
	private static final String CATEGORY_NOT_FOUND = "Category Not Found";
	private static final String SUBSCRIPTION_NOT_FOUND = "Subscription Not Found";

	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleCustomerNotFoundException(CustomerNotFoundException ex) {
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), CUSTOMER_NOT_FOUND);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

	}

	@ExceptionHandler(CategoryNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundException(CategoryNotFoundException ex) {
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), CATEGORY_NOT_FOUND);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

	}

	@ExceptionHandler(SubscriptionException.class)
	public ResponseEntity<ErrorResponse> handleSubscriptionException(SubscriptionException ex) {
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), SUBSCRIPTION_NOT_FOUND);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
		List<String> details = new ArrayList<String>();
		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
			details.add(error.getDefaultMessage());
		}
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

}
