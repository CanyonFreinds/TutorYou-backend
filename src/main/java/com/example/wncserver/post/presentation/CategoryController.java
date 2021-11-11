package com.example.wncserver.post.presentation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.wncserver.post.application.CategoryService;
import com.example.wncserver.post.domain.Category;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CategoryController {
	private final CategoryService categoryService;

	@GetMapping("/categories")
	public ResponseEntity<List<String>> getCategories() {
		return ResponseEntity.ok(
			categoryService.getCategories()
				.stream()
				.map(Category::getName)
				.collect(Collectors.toList())
		);
	}
}
