package com.example.wncserver.user.presentation.dto.teacher;

import java.util.List;
import java.util.stream.Collectors;

import com.example.wncserver.user.domain.User;
import com.querydsl.core.QueryResults;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeacherPageResponse {
	private final List<TeacherResponse> results;
	private final long total;
	private final long pageSize;
	private final long pageNumber;

	public static TeacherPageResponse from(QueryResults<User> result) {
		return TeacherPageResponse.builder()
			.results(result.getResults().stream().map(TeacherResponse::from).collect(Collectors.toList()))
			.total(result.getTotal())
			.pageSize((long)Math.ceil(((double)result.getTotal() / result.getLimit())))
			.pageNumber((result.getOffset() / result.getLimit()))
			.build();
	}
}
