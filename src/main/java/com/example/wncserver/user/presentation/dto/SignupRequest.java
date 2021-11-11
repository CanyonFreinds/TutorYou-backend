package com.example.wncserver.user.presentation.dto;

import java.util.List;

import com.example.wncserver.career.presentation.dto.CareerRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequest {
	private String email;
	private String password;
	private String name;
	private String role;
	private List<CareerRequest> careers;
}
