package com.example.wncserver.chat.presentation.dto;

import com.example.wncserver.chat.domain.Chat;
import com.example.wncserver.user.domain.User;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatResponse {
	private final Long chatRoomId;
	private final Long senderId;
	private final Long receiverId;
	private final String senderName;
	private final String receiverName;
	private final String senderImageSrc;
	private final String receiverImageSrc;
	private final String message;

	public static ChatResponse from(Chat chat, User sender, User receiver) {
		return ChatResponse.builder()
			.chatRoomId(chat.getChatRoom().getId())
			.senderId(sender.getId())
			.senderName(sender.getName())
			.senderImageSrc(sender.getImageUrl())
			.receiverId(receiver.getId())
			.receiverName(receiver.getName())
			.receiverImageSrc(receiver.getImageUrl())
			.message(chat.getMessage())
			.build();
	}
}
