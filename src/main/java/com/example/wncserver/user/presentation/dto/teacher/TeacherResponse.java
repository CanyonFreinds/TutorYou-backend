package com.example.wncserver.user.presentation.dto.teacher;

import java.util.List;
import java.util.stream.Collectors;

import com.example.wncserver.career.presentation.dto.CareerResponse;
import com.example.wncserver.user.domain.User;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeacherResponse {
	private final Long id;
	private final String name;
	private final double point;
	private final int studentCount;
	private final String imageSrc;
	private final List<CareerResponse> careers;

	public static TeacherResponse from(User teacher) {
		return TeacherResponse.builder()
			.id(teacher.getId())
			.name(teacher.getName())
			.point(teacher.getPoint())
			.studentCount(teacher.getStudentCount())
			.imageSrc(teacher.getImageUrl())
			.careers(teacher.getCareers().stream().map(CareerResponse::from).collect(Collectors.toList()))
			.build();
	}
}
