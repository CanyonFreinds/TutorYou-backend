package com.example.wncserver.user.presentation.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.example.wncserver.career.presentation.dto.CareerResponse;
import com.example.wncserver.user.domain.User;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
	private final Long id;
	private final String email;
	private final String name;
	private final String role;
	private final List<CareerResponse> careers;

	public static UserResponse from(final User user) {
		return UserResponse.builder()
			.id(user.getId())
			.name(user.getName())
			.email(user.getEmail())
			.role(user.getRole().toString())
			.careers(user.getCareers().stream().map(CareerResponse::from).collect(Collectors.toList()))
			.build();
	}
}
