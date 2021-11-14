package com.example.wncserver.chat.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomRequest {
	private Long teacherId;
	private Long studentId;
}
