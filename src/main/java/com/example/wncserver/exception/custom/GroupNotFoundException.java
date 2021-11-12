package com.example.wncserver.exception.custom;

public class GroupNotFoundException extends RuntimeException {
	public GroupNotFoundException() {
		super("해당 그룹이 존재하지 않습니다.");
	}
}
