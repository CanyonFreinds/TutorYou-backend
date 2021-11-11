package com.example.wncserver.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@NoArgsConstructor
@Getter
@Setter
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "image_url")
	private String imageUrl;

	@Column(name = "point")
	private double point;

	@Column(name = "ban_count")
	private int banCount;

	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private Role role;

	public static User createUser(String email, String password, String name, Role authority){
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setName(name);
		user.setRole(authority);
		user.setBanCount(0);
		user.setPoint(0.0);
		user.setImageUrl("");
		return user;
	}
}