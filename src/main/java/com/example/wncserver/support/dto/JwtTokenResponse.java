package com.example.wncserver.support.dto;

import lombok.Getter;

@Getter
public class JwtTokenResponse {
	private final Long userId;
	private final String accessToken;

	public JwtTokenResponse(final Long userId, final String accessToken) {
		this.userId = userId;
		this.accessToken = accessToken;
	}
}
