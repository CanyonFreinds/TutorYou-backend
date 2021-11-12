package com.example.wncserver.group.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.wncserver.post.domain.Post;
import com.example.wncserver.user.domain.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "groups")
@NoArgsConstructor
@Getter
@Setter
public class Group {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "groups_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User teacher;

	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
	private List<StudentGroup> students = new ArrayList<>();

	@OneToOne(mappedBy = "group")
	private Post post;

	public void setTeacher(User teacher) {
		this.teacher = teacher;
		teacher.getGroups().add(this);
	}

	public static Group createGroup(User teacher) {
		Group group = new Group();
		group.setTeacher(teacher);
		return group;
	}
}
