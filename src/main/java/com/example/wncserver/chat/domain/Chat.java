package com.example.wncserver.chat.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.wncserver.user.domain.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chat")
@NoArgsConstructor
@Getter
@Setter
public class Chat {
	@Id
	@Column(name = "chat_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "chat_room_id")
	private ChatRoom chatRoom;

	@Column(name = "message")
	private String message;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	public void setChatRoom(ChatRoom chatRoom) {
		this.chatRoom = chatRoom;
		chatRoom.getChats().add(this);
	}

	public static Chat createChat(ChatRoom chatRoom) {
		Chat chat = new Chat();
		chat.setChatRoom(chatRoom);
		chat.setCreatedAt(LocalDateTime.now());
		return chat;
	}
}
