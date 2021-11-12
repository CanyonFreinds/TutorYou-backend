package com.example.wncserver.exception.custom;

public class StudentGroupNotFoundException extends RuntimeException {
	public StudentGroupNotFoundException() {
		super("해당 학생이 해당 그룹에 속해있지 않습니다.");
	}
}
