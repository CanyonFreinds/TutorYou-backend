package com.example.wncserver.career.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.wncserver.career.application.CareerService;
import com.example.wncserver.career.presentation.dto.CareerRequest;
import com.example.wncserver.career.presentation.dto.CareerResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CareerController {
	private final CareerService careerService;

	@PutMapping("users/{userId}/careers/{careerId}")
	public ResponseEntity<CareerResponse> changeUserCareer(@PathVariable final Long userId,
		@PathVariable final Long careerId, @RequestBody final CareerRequest updateRequest) {
		final CareerResponse result = careerService.changeUserCareer(careerId, updateRequest);
		return ResponseEntity.ok(result);
	}

	@PostMapping("users/{userId}/careers")
	public ResponseEntity<CareerResponse> addUserCareer(@PathVariable final Long userId,
		@RequestBody final CareerRequest createRequest) {
		final CareerResponse result = careerService.addUserCareer(userId, createRequest);
		return ResponseEntity.ok(result);
	}

	@DeleteMapping("users/{userId}/careers/{careerId}")
	public ResponseEntity<Void> deleteUserCareer(@PathVariable final Long userId, @PathVariable final Long careerId) {
		careerService.deleteCareer(careerId);
		return ResponseEntity.noContent().build();
	}
}
