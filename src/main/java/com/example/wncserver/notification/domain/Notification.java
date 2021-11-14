package com.example.wncserver.notification.domain;

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
@Table(name = "notification")
@NoArgsConstructor
@Getter
@Setter
public class Notification {
	@Id
	@Column(name = "notification_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "message")
	private String message;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User receiver;

	public void setReceiver(User receiver) {
		this.receiver = receiver;
		receiver.getNotifications().add(this);
	}

	public static Notification createNotification(User receiver, String message) {
		Notification notification = new Notification();
		notification.setReceiver(receiver);
		notification.setMessage(message);
		return notification;
	}
}
