package com.example.wncserver.user.presentation.dto.admin;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.example.wncserver.user.domain.User;
import com.example.wncserver.user.presentation.dto.teacher.TeacherResponse;
import com.querydsl.core.QueryResults;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminResponse {
	private final List<AdminTeacherResponse> results;
	private final long total;
	private final long pageSize;
	private final long pageNumber;

	public static AdminResponse from(QueryResults<User> result) {
		return AdminResponse.builder()
			.results(result.getResults().stream().map(AdminTeacherResponse::from).collect(Collectors.toList()))
			.total(result.getTotal())
			.pageSize((long)Math.ceil(((double)result.getTotal() / result.getLimit())))
			.pageNumber((result.getOffset() / result.getLimit()))
			.build();
	}
}
