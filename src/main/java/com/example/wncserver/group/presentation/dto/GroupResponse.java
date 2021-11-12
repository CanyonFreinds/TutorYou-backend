package com.example.wncserver.group.presentation.dto;

import java.util.List;

import com.example.wncserver.group.domain.Group;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GroupResponse {
	private final Long groupId;
	private final String teacherName;
	private final List<String> studentNames;

	public static GroupResponse from(Group group, List<String> studentNames) {
		return GroupResponse.builder()
			.groupId(group.getId())
			.teacherName(group.getTeacher().getName())
			.studentNames(studentNames)
			.build();
	}
}
