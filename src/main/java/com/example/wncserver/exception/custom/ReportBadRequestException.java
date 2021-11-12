package com.example.wncserver.exception.custom;

public class ReportBadRequestException extends RuntimeException {
	public ReportBadRequestException() {
		super("신고는 한번 만 할 수 있습니다.");
	}
}
