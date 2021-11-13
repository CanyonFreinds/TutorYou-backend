package com.example.wncserver.user.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.wncserver.user.presentation.dto.teacher.TeacherPageResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public TeacherPageResponse findAllByQuery(String query, String sort, String order, Pageable pageable) {
		BooleanBuilder builder = new BooleanBuilder();
		builder.or(QUser.user.name.like("%" + query + "%")
			.and(QUser.user.role.eq(Role.ROLE_TEACHER)));

		QueryResults<User> result = jpaQueryFactory.select(QUser.user)
			.from(QUser.user)
			.where(builder)
			.distinct()
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(getOrderSpecifier(sort, order))
			.fetchResults();

		return TeacherPageResponse.from(result);
	}

	private OrderSpecifier<?> getOrderSpecifier(final String sort, final String order) {
		if (sort.equals("") && order.equals("desc")) {
			return QUser.user.id.desc();
		} else if (sort.equals("studentCount") && order.equals("asc")) {
			return QUser.user.studentCount.asc();
		} else if (sort.equals("studentCount") && order.equals("desc")) {
			return QUser.user.studentCount.desc();
		} else if (sort.equals("point") && order.equals("asc")) {
			return QUser.user.point.asc();
		} else if (sort.equals("point") && order.equals("desc")) {
			return QUser.user.point.desc();
		} else {
			return QUser.user.id.asc();
		}
	}
}
