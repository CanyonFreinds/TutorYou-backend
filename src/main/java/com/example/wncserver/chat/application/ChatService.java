package com.example.wncserver.chat.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.wncserver.chat.domain.Chat;
import com.example.wncserver.chat.domain.ChatRepository;
import com.example.wncserver.chat.domain.ChatRoom;
import com.example.wncserver.chat.domain.ChatRoomRepository;
import com.example.wncserver.chat.domain.ChatRoomUser;
import com.example.wncserver.chat.domain.ChatRoomUserRepository;
import com.example.wncserver.chat.presentation.dto.ChatRequest;
import com.example.wncserver.chat.presentation.dto.ChatResponse;
import com.example.wncserver.chat.presentation.dto.ChatRoomRequest;
import com.example.wncserver.chat.presentation.dto.ChatRoomResponse;
import com.example.wncserver.exception.custom.ChatRoomNotFoundException;
import com.example.wncserver.exception.custom.UserNotFoundException;
import com.example.wncserver.notification.application.NotificationService;
import com.example.wncserver.user.domain.Role;
import com.example.wncserver.user.domain.User;
import com.example.wncserver.user.domain.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
	private final ChatRepository chatRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatRoomUserRepository chatRoomUserRepository;
	private final UserRepository userRepository;
	private final NotificationService notificationService;

	@Transactional
	public ChatRoomResponse createChatRoom(final ChatRoomRequest chatRoomRequest) {
		final Long studentId = chatRoomRequest.getStudentId();
		final Long teacherId = chatRoomRequest.getTeacherId();
		User teacher = userRepository.findById(teacherId).orElseThrow(UserNotFoundException::new);
		User student = userRepository.findById(studentId).orElseThrow(UserNotFoundException::new);
		ChatRoom chatRoom = ChatRoom.createChatRoom();
		chatRoomRepository.save(chatRoom);
		setChatRoomToUser(chatRoom, teacher, student);
		return ChatRoomResponse.from(chatRoom, teacher, student);
	}

	private void setChatRoomToUser(final ChatRoom chatRoom, final User teacher, final User student) {
		ChatRoomUser chatRoomInStudent = ChatRoomUser.createChatRoomUser(student, chatRoom);
		ChatRoomUser chatRoomInTeacher = ChatRoomUser.createChatRoomUser(teacher, chatRoom);
		chatRoomUserRepository.save(chatRoomInStudent);
		chatRoomUserRepository.save(chatRoomInTeacher);
	}

	@Transactional
	public Long createChat(final ChatRequest chatRequest) {
		final Long chatRoomId = chatRequest.getChatRoomId();
		final Long senderId = chatRequest.getSenderId();
		final Long receiverId = chatRequest.getReceiverId();
		final String message = chatRequest.getMessage();
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(ChatRoomNotFoundException::new);
		Chat chat = Chat.createChat(chatRoom, senderId, receiverId, message);
		chatRepository.save(chat);
		notificationService.createNotification(receiverId, "채팅이 도착했습니다.");
		return chat.getId();
	}

	@Transactional(readOnly = true)
	public List<ChatResponse> getChats(final Long chatRoomId) {
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(ChatRoomNotFoundException::new);
		List<Chat> chats = chatRoom.getChats();
		List<ChatResponse> responses = new ArrayList<>();
		for (Chat chat : chats) {
			Long senderId = chat.getSenderId();
			User sender = userRepository.findById(senderId).orElseThrow(UserNotFoundException::new);
			Long receiverId = chat.getReceiverId();
			User receiver = userRepository.findById(receiverId).orElseThrow(UserNotFoundException::new);
			responses.add(ChatResponse.from(chat, sender, receiver));
		}
		return responses;
	}

	@Transactional
	public List<ChatRoomResponse> getChatRooms(Long userId) {
		List<ChatRoomUser> chatRoomUsers = chatRoomUserRepository.findAllByChatter_Id(userId);
		Map<Long, User> students = new HashMap<>();
		Map<Long, User> teachers = new HashMap<>();
		Set<ChatRoomUser> chatRoomUsersAll = new HashSet<>();
		for (ChatRoomUser chatRoomUser : chatRoomUsers) {
			List<ChatRoomUser> temp = chatRoomUserRepository.findAllByChatRoom_Id(chatRoomUser.getChatRoom().getId());
			chatRoomUsersAll.addAll(temp);
		}
		List<ChatRoomResponse> responses = new ArrayList<>();
		for (ChatRoomUser chatRoomUser : chatRoomUsersAll) {
			if(chatRoomUser.getChatter().getRole().equals(Role.ROLE_TEACHER)) {
				teachers.put(chatRoomUser.getChatRoom().getId(), chatRoomUser.getChatter());
			} else if(chatRoomUser.getChatter().getRole().equals(Role.ROLE_STUDENT)) {
				students.put(chatRoomUser.getChatRoom().getId(), chatRoomUser.getChatter());
			}
		}
		for (ChatRoomUser chatRoomUser : chatRoomUsers) {
			ChatRoom chatRoom = chatRoomUser.getChatRoom();
			responses.add(ChatRoomResponse.from(chatRoom, teachers.get(chatRoom.getId()),students.get(chatRoom.getId())));
		}
		return responses;
	}
}
