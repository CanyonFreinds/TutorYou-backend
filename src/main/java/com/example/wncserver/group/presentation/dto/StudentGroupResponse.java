package com.example.wncserver.group.presentation.dto;

import java.util.stream.Collectors;

import com.example.wncserver.group.domain.StudentGroup;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudentGroupResponse {
	private final Long groupId;
	private final String teacherName;
	private final String student;

	public static StudentGroupResponse from(StudentGroup studentGroup) {
		return StudentGroupResponse.builder()
			.groupId(studentGroup.getGroup().getId())
			.teacherName(studentGroup.getGroup().getTeacher().getName())
			.student(studentGroup.getStudent().getName())
			.build();
	}
}
