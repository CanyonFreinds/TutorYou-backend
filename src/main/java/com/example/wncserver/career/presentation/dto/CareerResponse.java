package com.example.wncserver.career.presentation.dto;

import com.example.wncserver.career.domain.Career;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CareerResponse {
	private final Long careerId;
	private final String content;
	private final String careerType;

	public static CareerResponse from(final Career career) {
		return CareerResponse.builder()
			.careerId(career.getId())
			.content(career.getContent())
			.careerType(career.getCareerType().toString())
			.build();
	}
}
