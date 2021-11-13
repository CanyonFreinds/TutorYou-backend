package com.example.wncserver.user.presentation;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.wncserver.user.application.UserService;
import com.example.wncserver.user.presentation.dto.admin.AdminResponse;
import com.example.wncserver.user.presentation.dto.admin.AdminTeacherResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AdminController {
	private final UserService userService;

	@GetMapping("/admin/teacher/blacklist")
	public ResponseEntity<AdminResponse> getBlackList(final Pageable pageable) {
		return ResponseEntity.ok(userService.getTeacherListByIsBaned(true, pageable));
	}

	@GetMapping("/admin/teacher")
	public ResponseEntity<AdminResponse> getTeacherList(final Pageable pageable) {
		return ResponseEntity.ok(userService.getTeacherListByIsBaned(false, pageable));
	}

	@PutMapping("/admin/teacher/{teacherId}")
	public ResponseEntity<AdminTeacherResponse> changeTeacherBanState(@PathVariable final Long teacherId) {
		return ResponseEntity.ok(userService.manageBanTeacher(teacherId));
	}
}
