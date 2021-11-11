package com.example.wncserver.user.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.wncserver.career.domain.Career;

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

	@OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
	private List<Career> careers = new ArrayList<>();

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