package com.example.wncserver.exception.custom;

public class EmailDuplicateException extends RuntimeException {
	public EmailDuplicateException() {
		super("Email이 중복되었습니다.");
	}
}
