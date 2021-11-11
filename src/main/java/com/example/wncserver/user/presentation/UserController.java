package com.example.wncserver.user.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.wncserver.exception.custom.EmailDuplicateException;
import com.example.wncserver.user.application.UserService;
import com.example.wncserver.user.domain.UserPrincipal;
import com.example.wncserver.user.presentation.dto.UserNameUpdateRequest;
import com.example.wncserver.user.presentation.dto.UserPasswordUpdateRequest;
import com.example.wncserver.user.presentation.dto.UserResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
	private final UserService userService;

	@GetMapping("/users/validation")
	public ResponseEntity<Void> validateDuplicationEmailOrNickname(@RequestParam final String email) {
		if (userService.isDuplicateEmail(email)) {
			throw new EmailDuplicateException();
		}
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/users/{userId}")
	public ResponseEntity<Void> leaveMembership(@PathVariable final Long userId) {
		userService.deleteUser(userId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/users/self")
	public ResponseEntity<UserResponse> getSelfInformation(@AuthenticationPrincipal final UserPrincipal userPrincipal) {
		final UserResponse response = userService.getUserInformation(userPrincipal.getId());
		return ResponseEntity.ok(response);
	}

	@PutMapping("/users/{userId}/password")
	public ResponseEntity<Boolean> changeUserPassword(@PathVariable final Long userId,
		@RequestBody final UserPasswordUpdateRequest updateRequest) {
		final boolean result = userService.changeUserPassword(updateRequest, userId);
		return ResponseEntity.ok(result);
	}

	@PutMapping("/users/{userId}/name")
	public ResponseEntity<Boolean> changeUserName(@PathVariable final Long userId,
		@RequestBody final UserNameUpdateRequest updateRequest) {
		final boolean result = userService.changeUserName(updateRequest, userId);
		return ResponseEntity.ok(result);
	}
}
