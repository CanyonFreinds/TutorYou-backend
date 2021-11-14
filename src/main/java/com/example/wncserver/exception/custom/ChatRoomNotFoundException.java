package com.example.wncserver.exception.custom;

public class ChatRoomNotFoundException extends RuntimeException{
	public ChatRoomNotFoundException() {
		super("해당 채팅방이 존재하지 않습니다.");
	}
}
