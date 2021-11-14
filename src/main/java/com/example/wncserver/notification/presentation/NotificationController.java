package com.example.wncserver.notification.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.wncserver.notification.application.NotificationService;
import com.example.wncserver.support.handler.NotificationHandler;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class NotificationController {
	private final NotificationService notificationService;

	@DeleteMapping("/notifications/{notificationId}")
	public ResponseEntity<Void> deleteNotification(@PathVariable final Long notificationId) {
		notificationService.deleteNotification(notificationId);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/notifications/test/{userId}")
	public ResponseEntity<Long> createNotification(@PathVariable final Long userId) {
		return ResponseEntity.ok(notificationService.createNotification(userId, "테스트 알림입니다."));
	}
}
