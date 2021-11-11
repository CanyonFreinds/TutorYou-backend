package com.example.wncserver.support.interceptor;

public enum CustomStatus {
	INVALID_TOKEN(4999), IS_LOCKED_TOKEN(4888);

	private final int code;

	CustomStatus(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
