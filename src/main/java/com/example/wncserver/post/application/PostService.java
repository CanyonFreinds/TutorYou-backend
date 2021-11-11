package com.example.wncserver.post.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.wncserver.exception.custom.CategoryNotFoundException;
import com.example.wncserver.exception.custom.PostNotFoundException;
import com.example.wncserver.exception.custom.UserNotFoundException;
import com.example.wncserver.category.domain.Category;
import com.example.wncserver.category.domain.CategoryRepository;
import com.example.wncserver.post.domain.Post;
import com.example.wncserver.post.domain.PostQueryRepository;
import com.example.wncserver.post.domain.PostRepository;
import com.example.wncserver.post.presentation.dto.PostListResponse;
import com.example.wncserver.post.presentation.dto.PostRequest;
import com.example.wncserver.post.presentation.dto.PostResponse;
import com.example.wncserver.user.domain.User;
import com.example.wncserver.user.domain.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
	private final UserRepository userRepository;
	private final PostRepository postRepository;
	private final PostQueryRepository postQueryRepository;
	private final CategoryRepository categoryRepository;

	@Transactional
	public PostResponse savePost(final PostRequest createRequest) {
		final User author = userRepository.findById(createRequest.getUserId()).orElseThrow(UserNotFoundException::new);
		final Category category = categoryRepository.findByName(createRequest.getCategoryName()).orElseThrow(
			CategoryNotFoundException::new);
		Post post = Post.createPost(author, category, createRequest);
		return PostResponse.from(postRepository.save(post));
	}

	@Transactional
	public PostResponse updatePost(final Long postId, final PostRequest updateRequest) {
		final Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
		final Category category = categoryRepository.findByName(updateRequest.getCategoryName()).orElseThrow(
			CategoryNotFoundException::new);
		post.updatePost(category, updateRequest);
		return PostResponse.from(post);
	}

	@Transactional
	public void deletePost(final Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
		postRepository.delete(post);
	}

	@Transactional(readOnly = true)
	public PostResponse readPost(final Long postId) {
		Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
		return PostResponse.from(post);
	}

	@Transactional(readOnly = true)
	public List<PostListResponse> readPosts(Pageable pageable) {
		Page<Post> posts = postRepository.findAll(pageable);
		return posts.stream().map(PostListResponse::from).collect(Collectors.toList());
	}

	@Transactional
	public List<PostListResponse> search(String searchType, String query, Pageable pageable) {
		Page<Post> posts = postQueryRepository.findAllByQuery(query, searchType, pageable);
		return posts.stream().map(PostListResponse::from).collect(Collectors.toList());
	}
}
