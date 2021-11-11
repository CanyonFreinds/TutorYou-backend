package com.example.wncserver.exception.custom;

public class PostNotFoundException extends RuntimeException {
	public PostNotFoundException() {
		super("해당 게시글이 존재하지 않습니다.");
	}
}
