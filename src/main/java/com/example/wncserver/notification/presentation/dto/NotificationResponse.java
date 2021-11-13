package com.example.wncserver.notification.presentation.dto;

import com.example.wncserver.notification.domain.Notification;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationResponse {
	private final Long notificationId;
	private final String message;

	public static NotificationResponse from(Notification notification) {
		return NotificationResponse.builder()
			.notificationId(notification.getId())
			.message(notification.getMessage())
			.build();
	}
}
