package com.example.wncserver.post.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.wncserver.post.presentation.dto.PostRequest;
import com.example.wncserver.user.domain.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "post")
@NoArgsConstructor
@Getter
@Setter
public class Post extends Auditor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User author;

	@Column(name = "title", nullable = false)
	private String title;

	@Lob
	@Column(name = "content")
	private String content;

	@OneToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@Enumerated(EnumType.STRING)
	@Column(name = "post_type")
	private PostType postType;

	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "end_date")
	private LocalDate endDate;

	@Column(name = "total_student_count")
	private int totalStudentCount;

	@Column(name = "applicant_count")
	private int applicantCount;

	public void setAuthor(User author) {
		this.author = author;
		author.getPosts().add(this);
	}

	public static Post createPost(User author, Category category, PostRequest request) {
		Post post = new Post();
		post.setAuthor(author);
		post.setCategory(category);
		post.setTitle(request.getTitle());
		post.setContent(request.getContent());
		post.setPostType(PostType.valueOf(request.getPostType()));
		post.setTotalStudentCount(request.getTotalStudentCount());
		post.setApplicantCount(0);
		if (PostType.valueOf(request.getPostType()).equals(PostType.ONE_TO_MANY)) {
			post.setStartDate(LocalDate.parse(request.getStartDate()));
			post.setEndDate(LocalDate.parse(request.getEndDate()));
		}
		return post;
	}

	public void updatePost(Category category, PostRequest request) {
		this.setCategory(category);
		this.setTitle(request.getTitle());
		this.setContent(request.getContent());
		this.setPostType(PostType.valueOf(request.getPostType()));
		this.setTotalStudentCount(request.getTotalStudentCount());
		if (PostType.valueOf(request.getPostType()).equals(PostType.ONE_TO_MANY)) {
			this.setStartDate(LocalDate.parse(request.getStartDate()));
			this.setEndDate(LocalDate.parse(request.getEndDate()));
		}
	}
}
