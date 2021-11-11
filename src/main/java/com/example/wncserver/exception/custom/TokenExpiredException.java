package com.example.wncserver.exception.custom;

public class TokenExpiredException extends RuntimeException{
	public TokenExpiredException() {
		super("유효시간이 지난 토큰입니다.");
	}
}
