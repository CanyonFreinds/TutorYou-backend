package com.example.wncserver.user.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequest {
	private String email;
	private String password;
	private String name;
	private String role;
}
