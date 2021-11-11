package com.example.wncserver.post.presentation.dto;

import lombok.Getter;

@Getter
public class PostRequest {
	private String title;
	private String content;
	private Long userId;
	private String postType;
	private String categoryName;
	private int totalStudentCount;
	private String startDate;
	private String endDate;
}
