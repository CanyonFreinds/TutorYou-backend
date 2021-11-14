package com.example.wncserver.support.dto;

import java.util.List;

import com.example.wncserver.notification.presentation.dto.NotificationResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationMessage {
	private String type;
	private Long userId;
	private List<NotificationResponse> notifications;

	public NotificationMessage(String type, Long userId, List<NotificationResponse> notifications) {
		this.type = type;
		this.userId = userId;
		this.notifications = notifications;
	}
}
