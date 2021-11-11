package com.example.wncserver.post.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public Page<Post> findAllByQuery(String query, String type, Pageable pageable) {
		BooleanBuilder builder = makeBooleanBuilder(query, type);
		QueryResults<Post> result = jpaQueryFactory.select(QPost.post)
			.from(QPost.post)
			.where(builder)
			.distinct()
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(QPost.post.id.asc())
			.fetchResults();

		return new PageImpl<>(result.getResults(),pageable,result.getTotal());
	}

	private BooleanBuilder makeBooleanBuilder(String query, String type) {
		BooleanBuilder builder = new BooleanBuilder();
		switch (type.toUpperCase()) {
			case "TITLE":
				builder.or(QPost.post.title.like("%" + query + "%"));
				break;
			case "CONTENT":
				builder.or(QPost.post.content.like("%" + query + "%"));
				break;
			case "AUTHOR":
				builder.or(QPost.post.author.name.like("%" + query + "%"));
				break;
			case "CATEGORY":
				builder.or(QPost.post.category.name.like("%" + query + "%"));
				break;
			default:
				break;
		}
		return builder;
	}
}