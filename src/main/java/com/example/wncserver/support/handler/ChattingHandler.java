package com.example.wncserver.support.handler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.wncserver.notification.domain.Notification;
import com.example.wncserver.notification.domain.NotificationRepository;
import com.example.wncserver.notification.presentation.dto.NotificationResponse;
import com.example.wncserver.support.dto.WebSocketMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChattingHandler extends TextWebSocketHandler {
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final NotificationRepository notificationRepository;
	private final ConcurrentMap<String, Long> sessionIdToUserId = new ConcurrentHashMap<>();
	private final ConcurrentMap<Long, WebSocketSession> userIdToSession = new ConcurrentHashMap<>();

	private static final String TYPE_SEND_USER_ID = "sendUserId";
	private static final String TYPE_RECEIVE_USER_ID = "receiveUserId";
	private static final String TYPE_SEND_NOTIFICATION = "sendNotification";

	@Override
	public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) {
		log.info("Socket 이 연결 해제되었습니다.." + session.getId());
		log.info(status.toString());
		Long userId = sessionIdToUserId.get(session.getId());
		userIdToSession.remove(userId);
		sessionIdToUserId.remove(session.getId());
	}

	@Override
	public void afterConnectionEstablished(final WebSocketSession session) {
		log.info("Socket 이 연결되었습니다." + session.getId());
		sendMessage(session, new WebSocketMessage(TYPE_SEND_USER_ID, null, null));
	}

	@Override
	protected void handleTextMessage(final WebSocketSession session, final TextMessage textMessage) {
		try {
			WebSocketMessage message = objectMapper.readValue(textMessage.getPayload(), WebSocketMessage.class);
			String type = message.getType();
			Long userId = message.getUserId();

			if (TYPE_RECEIVE_USER_ID.equals(type)) {
				sessionIdToUserId.put(session.getId(), userId);
				userIdToSession.put(userId, session);
				List<Notification> notifications = notificationRepository.findAllByReceiver_Id(userId);
				List<NotificationResponse> responses = notifications.stream()
					.map(NotificationResponse::from)
					.collect(Collectors.toList());
				sendMessage(session, new WebSocketMessage(TYPE_SEND_NOTIFICATION, userId, responses));
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	public void sendNotification(Long userId, Notification notification) {
		if (userIdToSession.containsKey(userId)) {
			WebSocketSession session = userIdToSession.get(userId);
			List<NotificationResponse> response = List.of(NotificationResponse.from(notification));
			sendMessage(session, new WebSocketMessage(TYPE_SEND_NOTIFICATION, userId, response));
		}
	}

	private void sendMessage(WebSocketSession session, WebSocketMessage message) {
		try {
			String json = objectMapper.writeValueAsString(message);
			session.sendMessage(new TextMessage(json));
		} catch (IOException e) {
			log.debug("An error occured: {}", e.getMessage());
		}
	}
}
