package com.example.wncserver.support.handler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.wncserver.chat.application.ChatService;
import com.example.wncserver.chat.domain.Chat;
import com.example.wncserver.chat.presentation.dto.ChatRequest;
import com.example.wncserver.notification.domain.Notification;
import com.example.wncserver.notification.domain.NotificationRepository;
import com.example.wncserver.notification.presentation.dto.NotificationResponse;
import com.example.wncserver.support.dto.ChatMessage;
import com.example.wncserver.support.dto.NotificationMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChattingHandler extends TextWebSocketHandler {
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final ConcurrentMap<String, Long> sessionIdToUserId = new ConcurrentHashMap<>();
	private final ChatService chatService;
	private final ConcurrentMap<Long, WebSocketSession> userIdToSession = new ConcurrentHashMap<>();

	private static final String TYPE_SEND_USER_ID = "sendChat";
	private static final String TYPE_RECEIVE_USER_ID = "receiveUserId";
	private static final String TYPE_RECEIVE_CHAT = "receiveChat";
	private static final String TYPE_SEND_CHAT = "sendChat";

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
		sendMessage(session, new ChatMessage(TYPE_SEND_USER_ID, null, null, null, null));
	}

	@Override
	protected void handleTextMessage(final WebSocketSession session, final TextMessage textMessage) {
		try {
			ChatMessage message = objectMapper.readValue(textMessage.getPayload(), ChatMessage.class);
			String type = message.getType();
			Long chatRoomId = message.getChatRoomId();
			Long senderId = message.getSenderId();
			Long receiverId = message.getReceiverId();
			String msg = message.getMessage();

			switch (type) {
				case TYPE_RECEIVE_USER_ID:
					sessionIdToUserId.put(session.getId(), senderId);
					userIdToSession.put(senderId, session);
					break;
				case TYPE_SEND_CHAT:
					chatService.createChat(new ChatRequest(chatRoomId, senderId, receiverId, msg));
					sendChat(chatRoomId, senderId, receiverId, msg);
					break;
				default:
					break;
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	public void sendChat(final Long chatRoomId, final Long senderId, final Long receiverId, final String msg) {
		if (userIdToSession.containsKey(receiverId)) {
			WebSocketSession session = userIdToSession.get(receiverId);
			sendMessage(session, new ChatMessage(TYPE_RECEIVE_CHAT, chatRoomId, senderId, receiverId, msg));
		}
	}

	private void sendMessage(WebSocketSession session, ChatMessage message) {
		try {
			String json = objectMapper.writeValueAsString(message);
			session.sendMessage(new TextMessage(json));
		} catch (IOException e) {
			log.debug("An error occured: {}", e.getMessage());
		}
	}
}
