package com.example.wncserver.notification.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.wncserver.exception.custom.NotificationNotFoundException;
import com.example.wncserver.exception.custom.UserNotFoundException;
import com.example.wncserver.notification.domain.Notification;
import com.example.wncserver.notification.domain.NotificationRepository;
import com.example.wncserver.support.handler.NotificationHandler;
import com.example.wncserver.user.domain.User;
import com.example.wncserver.user.domain.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
	private final NotificationRepository notificationRepository;
	private final UserRepository userRepository;
	private final NotificationHandler notificationHandler;

	@Transactional
	public void deleteNotification(final Long notificationId) {
		Notification notification = notificationRepository.findById(notificationId)
			.orElseThrow(NotificationNotFoundException::new);
		notificationRepository.delete(notification);
	}

	@Transactional
	public Long createNotification(final Long userId, final String message) {
		User receiver = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
		Notification notification = Notification.createNotification(receiver, message);
		notificationRepository.save(notification);
		notificationHandler.sendNotification(receiver.getId(), notification);
		return notification.getId();
	}
}
