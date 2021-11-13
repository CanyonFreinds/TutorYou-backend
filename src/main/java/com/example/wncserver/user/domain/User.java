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
import com.example.wncserver.group.domain.Group;
import com.example.wncserver.group.domain.StudentGroup;
import com.example.wncserver.notification.domain.Notification;
import com.example.wncserver.post.domain.Post;

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

	@Column(name = "voter_count")
	private int voterCount;

	@Column(name = "student_count")
	private int studentCount;

	@Column(name = "ban_count")
	private int banCount;

	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private Role role;

	@Column(name = "is_baned")
	private boolean isBaned;

	@OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
	private List<Career> careers = new ArrayList<>();

	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
	private List<Post> posts = new ArrayList<>();

	@OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
	private List<Group> groups = new ArrayList<>();

	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
	private List<StudentGroup> studentGroups = new ArrayList<>();

	@OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
	private List<Notification> notifications = new ArrayList<>();

	public static User createUser(String email, String password, String name, Role role) {
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setName(name);
		user.setRole(role);
		user.setBanCount(0);
		user.setPoint(0.0);
		user.setVoterCount(0);
		user.setStudentCount(0);
		user.setBaned(false);
		user.setImageUrl("https://user-images.githubusercontent.com/55012742/141612238-4293f504-82fc-4689-8bc4-15bdb6dbe834.png");
		return user;
	}
}