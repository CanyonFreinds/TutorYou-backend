package com.example.wncserver.user.presentation.dto.teacher;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeacherReportRequest {
	private Long studentId;
	private Long groupId;
	private String msg;
}
