package com.example.wncserver.exception.custom;

public class RefreshTokenException extends RuntimeException{
	public RefreshTokenException() {
	}

	public RefreshTokenException(String message) {
		super(message);
	}
}
