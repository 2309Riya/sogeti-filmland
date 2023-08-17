package com.sogeti.filmland.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sogeti.filmland.model.request.ShareSubscriptionRequest;
import com.sogeti.filmland.model.request.SubscriptionRequest;
import com.sogeti.filmland.model.response.ResponseDTO;
import com.sogeti.filmland.service.ShareSubscriptionService;
import com.sogeti.filmland.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/sogeti/filmland")
@RequiredArgsConstructor
@Slf4j
public class SubscriptionController {

	private final SubscriptionService subscriptionService;
	private final ShareSubscriptionService shareSubscriptionService;

	@PostMapping("/subscribe")
	public ResponseEntity<ResponseDTO> subscribeToCategory(@Valid @RequestBody SubscriptionRequest request) {
		log.info("Request received to subscribe to Category");
		ResponseDTO responseDTO = subscriptionService.subscribeToCategory(request.getEmail(),
				request.getAvailableCategory());
		return ResponseEntity.ok(responseDTO);

	}

	@PostMapping("/shareCategory")
	public ResponseEntity<ResponseDTO> shareSubscription(@Valid @RequestBody ShareSubscriptionRequest request) {
		log.info("Request received to Share Subscription");

		ResponseDTO response = shareSubscriptionService.shareSubscription(request);
		return ResponseEntity.ok(response);
	}

}
