package com.example.wncserver.user.presentation.dto;

import com.example.wncserver.user.domain.User;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
	private final Long id;
	private final String email;
	private final String name;
	private final String authority;

	public static UserResponse from(final User user) {
		return UserResponse.builder()
			.id(user.getId())
			.name(user.getName())
			.email(user.getEmail())
			.authority(user.getRole().toString())
			.build();
	}
}
