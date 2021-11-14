package com.example.wncserver.support.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessage {
	private String type;
	private Long chatRoomId;
	private Long senderId;
	private Long receiverId;
	private String message;

	public ChatMessage(String type, Long chatRoomId, Long senderId, Long receiverId, String message) {
		this.type = type;
		this.chatRoomId = chatRoomId;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.message = message;
	}
}
