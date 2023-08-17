package com.sogeti.filmland.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SubscriptionException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 634665212662116732L;

	public SubscriptionException() {

	}
}
