package com.example.wncserver.exception.custom;

public class CareerNotFoundException extends RuntimeException {
	public CareerNotFoundException() {
		super("해당 경력이 존재하지 않습니다.");
	}
}
