package com.example.wncserver.chat.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {
	List<ChatRoomUser> findAllByChatter_Id(Long userId);
	List<ChatRoomUser> findAllByChatRoom_Id(Long chatRoomId);
}
