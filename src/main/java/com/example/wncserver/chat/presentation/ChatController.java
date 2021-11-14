package com.example.wncserver.chat.presentation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.wncserver.chat.application.ChatService;
import com.example.wncserver.chat.presentation.dto.ChatResponse;
import com.example.wncserver.chat.presentation.dto.ChatRoomRequest;
import com.example.wncserver.chat.presentation.dto.ChatRoomResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ChatController {
	private final ChatService chatService;

	@PostMapping("/chats/rooms")
	public ResponseEntity<ChatRoomResponse> createChatRoom(@RequestBody final ChatRoomRequest chatRoomRequest) {
		return ResponseEntity.ok(chatService.createChatRoom(chatRoomRequest));
	}

	@GetMapping("/chats/rooms")
	public ResponseEntity<List<ChatRoomResponse>> getChatRooms(@RequestParam final Long userId) {
		return ResponseEntity.ok(chatService.getChatRooms(userId));
	}

	@GetMapping("/chats/{chatRoomId}")
	public ResponseEntity<List<ChatResponse>> getChats(@PathVariable final Long chatRoomId) {
		return ResponseEntity.ok(chatService.getChats(chatRoomId));
	}
}
