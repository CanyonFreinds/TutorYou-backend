package com.example.wncserver.user.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefreshOrLogoutRequest {
	private String uuid;
	private String email;
	private String accessToken;
}
