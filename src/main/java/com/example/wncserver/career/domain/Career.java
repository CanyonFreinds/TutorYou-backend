package com.example.wncserver.career.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "career")
@NoArgsConstructor
@Getter
@Setter
public class Career {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "career_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User teacher;

	@Column(name = "content")
	private String content;

	@Enumerated(EnumType.STRING)
	@Column(name = "career_type")
	private CareerType careerType;

	public void setTeacher(User teacher) {
		this.teacher = teacher;
		teacher.getCareers().add(this);
	}

	public static Career createCareer(User teacher, String content, CareerType careerType) {
		Career career = new Career();
		career.setContent(content);
		career.setCareerType(careerType);
		career.setTeacher(teacher);
		return career;
	}

	public void updateCareer(String content, CareerType careerType) {
		this.setContent(content);
		this.setCareerType(careerType);
	}
}
