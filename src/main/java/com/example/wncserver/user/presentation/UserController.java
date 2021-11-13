package com.example.wncserver.user.presentation;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.wncserver.exception.custom.EmailDuplicateException;
import com.example.wncserver.user.application.UserService;
import com.example.wncserver.user.domain.UserPrincipal;
import com.example.wncserver.user.presentation.dto.UserNameUpdateRequest;
import com.example.wncserver.user.presentation.dto.UserPasswordUpdateRequest;
import com.example.wncserver.user.presentation.dto.UserResponse;
import com.example.wncserver.user.presentation.dto.teacher.TeacherPageResponse;
import com.example.wncserver.user.presentation.dto.teacher.TeacherPointUpdateRequest;
import com.example.wncserver.user.presentation.dto.teacher.TeacherReportRequest;

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

	@PutMapping("/users/{userId}/image")
	public ResponseEntity<String> changeUserProfileImage(@PathVariable final Long userId,
		@RequestPart("image") final MultipartFile multipartFile) {
		return ResponseEntity.ok(userService.changeUserImageUrl(multipartFile, userId));
	}

	@PutMapping("/users/{userId}/point")
	public ResponseEntity<Boolean> changeTeacherPoint(@PathVariable final Long userId,
		@RequestBody final TeacherPointUpdateRequest updateRequest) {
		final boolean result = userService.changeTeacherPoint(updateRequest, userId);
		return ResponseEntity.ok(result);
	}

	@PutMapping("/users/{userId}/report")
	public ResponseEntity<Boolean> reportTeacherPoint(@PathVariable final Long userId,
		@RequestBody final TeacherReportRequest request) {
		final boolean result = userService.reportTeacher(request, userId);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/users/teachers")
	public ResponseEntity<TeacherPageResponse> getTeachers(
		@RequestParam(value = "query") final Optional<String> optionalQuery,
		@RequestParam(value = "sort") final Optional<String> optionalSort,
		@RequestParam(value = "order") final Optional<String> optionalOrder, final Pageable pageable) {
		final String query = optionalQuery.orElse("");
		final String sort = optionalSort.orElse("asc");
		final String order = optionalOrder.orElse("");
		TeacherPageResponse result = userService.getTeachers(query, sort, order, pageable);
		return ResponseEntity.ok(result);
	}
}
