package com.example.wncserver.chat.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.wncserver.user.domain.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chat_room")
@NoArgsConstructor
@Getter
@Setter
public class ChatRoom {
	@Id
	@Column(name = "chat_room_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
	private List<Chat> chats = new ArrayList<>();

	@OneToMany(mappedBy = "chatter", cascade = CascadeType.ALL)
	private List<ChatRoomUser> users = new ArrayList<>();

	public static ChatRoom createChatRoom() {
		ChatRoom chatRoom = new ChatRoom();
		return chatRoom;
	}
}
