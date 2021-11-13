package com.example.wncserver.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.wncserver.exception.custom.AccessTokenException;
import com.example.wncserver.exception.custom.CareerNotFoundException;
import com.example.wncserver.exception.custom.CategoryNotFoundException;
import com.example.wncserver.exception.custom.EmailDuplicateException;
import com.example.wncserver.exception.custom.EvaluationBadRequestException;
import com.example.wncserver.exception.custom.ForbidToUserWritePost;
import com.example.wncserver.exception.custom.GroupNotFoundException;
import com.example.wncserver.exception.custom.ImageUploadFailureException;
import com.example.wncserver.exception.custom.InvalidPasswordException;
import com.example.wncserver.exception.custom.PostNotFoundException;
import com.example.wncserver.exception.custom.RefreshTokenException;
import com.example.wncserver.exception.custom.ReportBadRequestException;
import com.example.wncserver.exception.custom.StudentGroupNotFoundException;
import com.example.wncserver.exception.custom.TokenExpiredException;
import com.example.wncserver.exception.custom.TokenInvalidException;
import com.example.wncserver.exception.custom.UserNotFoundException;

@RestControllerAdvice
public class ExceptionAdvice {
	@ExceptionHandler({UserNotFoundException.class, CareerNotFoundException.class, CategoryNotFoundException.class
		, PostNotFoundException.class, GroupNotFoundException.class, StudentGroupNotFoundException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	private ResponseEntity<ErrorMsg> notFoundErrorHandler(HttpServletRequest request,
		final RuntimeException e) {
		ErrorMsg errorMsg = new ErrorMsg(e.getMessage(), HttpStatus.NOT_FOUND.toString(), request.getRequestURI());
		return new ResponseEntity<>(errorMsg, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({AccessTokenException.class, RefreshTokenException.class, TokenExpiredException.class,
		TokenInvalidException.class})
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	private ResponseEntity<ErrorMsg> unauthorizedErrorHandler(HttpServletRequest request,
		final RuntimeException e) {
		ErrorMsg errorInfo = new ErrorMsg(e.getMessage(), HttpStatus.UNAUTHORIZED.toString(),
			request.getRequestURI());
		return new ResponseEntity<>(errorInfo, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(EmailDuplicateException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	private ResponseEntity<ErrorMsg> conflictErrorHandler(HttpServletRequest request,
		final RuntimeException e) {
		ErrorMsg errorInfo = new ErrorMsg(e.getMessage(), HttpStatus.CONFLICT.toString(), request.getRequestURI());
		return new ResponseEntity<>(errorInfo, HttpStatus.CONFLICT);
	}

	@ExceptionHandler({InvalidPasswordException.class, ImageUploadFailureException.class,
		EvaluationBadRequestException.class, ReportBadRequestException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	private ResponseEntity<ErrorMsg> badRequestErrorHandler(HttpServletRequest request,
		final RuntimeException e) {
		ErrorMsg errorInfo = new ErrorMsg(e.getMessage(), HttpStatus.BAD_REQUEST.toString(), request.getRequestURI());
		return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ForbidToUserWritePost.class})
	@ResponseStatus(HttpStatus.FORBIDDEN)
	private ResponseEntity<ErrorMsg> forbiddenErrorHandler(HttpServletRequest request,
		final RuntimeException e) {
		ErrorMsg errorInfo = new ErrorMsg(e.getMessage(), HttpStatus.FORBIDDEN.toString(),
			request.getRequestURI());
		return new ResponseEntity<>(errorInfo, HttpStatus.FORBIDDEN);
	}
}
