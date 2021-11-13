package com.example.wncserver.user.presentation.dto.admin;

import com.example.wncserver.user.domain.User;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminTeacherResponse {
	private final String teacherName;
	private final double point;
	private final int banCount;
	private final boolean isBaned;

	public static AdminTeacherResponse from(User teacher) {
		return AdminTeacherResponse.builder()
			.teacherName(teacher.getName())
			.point(teacher.getPoint())
			.banCount(teacher.getBanCount())
			.isBaned(teacher.isBaned())
			.build();
	}
}
