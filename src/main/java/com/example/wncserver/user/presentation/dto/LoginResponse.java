package com.example.wncserver.user.presentation.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class LoginResponse {
	private final String accessToken;
	private final String uuid;
	private final Long userId;
	private final String email;
	private final String name;
	private final List<String> role;

	public LoginResponse(final String accessToken, final String uuid, final Long userId, final String email,
		final String name, final List<String> role) {
		this.accessToken = accessToken;
		this.uuid = uuid;
		this.userId = userId;
		this.email = email;
		this.name = name;
		this.role = role;
	}
}
