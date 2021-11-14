package com.example.wncserver.chat.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRequest {
	private Long chatRoomId;
	private Long senderId;
	private Long receiverId;
	private String message;

	public ChatRequest(Long chatRoomId, Long senderId, Long receiverId, String message) {
		this.chatRoomId = chatRoomId;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.message = message;
	}
}
