package com.example.wncserver.exception.custom;

public class NotificationNotFoundException extends RuntimeException {
	public NotificationNotFoundException() {
		super("해당 알림이 존재하지 않습니다.");
	}
}
