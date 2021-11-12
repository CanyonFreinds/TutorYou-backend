package com.example.wncserver.exception.custom;

public class EvaluationBadRequestException extends RuntimeException {
	public EvaluationBadRequestException() {
		super("이미 별점을 주었습니다.");
	}
}
