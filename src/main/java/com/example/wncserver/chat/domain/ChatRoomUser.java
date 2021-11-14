package com.example.wncserver.chat.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.wncserver.user.domain.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chat_room_user")
@NoArgsConstructor
@Getter
@Setter
public class ChatRoomUser {
	@Id
	@Column(name = "chat_room_user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User chatter;

	@ManyToOne
	@JoinColumn(name = "chat_room_id")
	private ChatRoom chatRoom;

	public void setChatter(User chatter) {
		this.chatter = chatter;
		chatter.getChatRooms().add(this);
	}

	public void setChatRoom(ChatRoom chatRoom) {
		this.chatRoom = chatRoom;
		chatRoom.getUsers().add(this);
	}

	public static ChatRoomUser createChatRoomUser(User chatter, ChatRoom chatRoom) {
		ChatRoomUser chatRoomUser = new ChatRoomUser();
		chatRoomUser.setChatter(chatter);
		chatRoomUser.setChatRoom(chatRoom);
		return chatRoomUser;
	}
}
