package com.example.wncserver.user.presentation.dto;

import lombok.Getter;

@Getter
public class UserPasswordUpdateRequest {
	private String beforePassword;
	private String afterPassword;
}
