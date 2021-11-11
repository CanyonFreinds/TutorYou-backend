package com.example.wncserver.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Getter;

@Getter
public class ErrorMsg {
	private String msg;
	private String statusCode;
	private String uriRequested;
	private String timestamp;

	public ErrorMsg(String msg, String statusCode, String uriRequested) {
		this.msg = msg;
		this.statusCode = statusCode;
		this.uriRequested = uriRequested;
		this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}
}
