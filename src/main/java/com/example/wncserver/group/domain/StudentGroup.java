package com.example.wncserver.group.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "student_group")
@NoArgsConstructor
@Getter
@Setter
public class StudentGroup {
	@Id
	@Column(name = "student_group_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "group_id")
	private Group group;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User student;

	public void setStudent(User student) {
		this.student = student;
		student.getStudentGroups().add(this);
	}

	public void setGroup(Group group) {
		this.group = group;
		group.getStudents().add(this);
	}

	public static StudentGroup createStudentGroup(User student, Group group) {
		StudentGroup studentGroup = new StudentGroup();
		studentGroup.setStudent(student);
		studentGroup.setGroup(group);
		return studentGroup;
	}
}
