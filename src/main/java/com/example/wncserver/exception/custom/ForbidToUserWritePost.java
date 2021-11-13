package com.example.wncserver.exception.custom;

public class ForbidToUserWritePost extends RuntimeException {
	public ForbidToUserWritePost() {
		super("글을 작성할 수 없습니다.");
	}
}
