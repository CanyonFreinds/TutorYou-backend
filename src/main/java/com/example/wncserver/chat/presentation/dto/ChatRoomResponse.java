package com.example.wncserver.chat.presentation.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.example.wncserver.chat.domain.ChatRoom;
import com.example.wncserver.chat.domain.ChatRoomUser;
import com.example.wncserver.user.domain.Role;
import com.example.wncserver.user.domain.User;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomResponse {
	private final Long chatRoomId;
	private final Long teacherId;
	private final Long studentId;
	private final String teacherName;
	private final String studentName;

	public static ChatRoomResponse from(ChatRoom chatRoom, User teacher, User student) {
		return ChatRoomResponse.builder()
			.chatRoomId(chatRoom.getId())
			.teacherId(teacher.getId())
			.teacherName(teacher.getName())
			.studentId(student.getId())
			.studentName(student.getName())
			.build();
	}

}
