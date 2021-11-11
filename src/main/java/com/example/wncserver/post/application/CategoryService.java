package com.example.wncserver.post.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.wncserver.post.domain.Category;
import com.example.wncserver.post.domain.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
	private final CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public List<Category> getCategories() {
		return categoryRepository.findAll();
	}
}
