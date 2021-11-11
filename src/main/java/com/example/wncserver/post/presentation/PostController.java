package com.example.wncserver.post.presentation;

import java.net.URI;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.wncserver.post.application.PostService;
import com.example.wncserver.post.presentation.dto.PostListResponse;
import com.example.wncserver.post.presentation.dto.PostRequest;
import com.example.wncserver.post.presentation.dto.PostResponse;
import com.example.wncserver.support.util.S3UploadUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PostController {
	private final PostService postService;
	private final S3UploadUtil s3UploadUtil;

	@PostMapping("/posts")
	public ResponseEntity<PostResponse> save(@RequestBody PostRequest postRequest) {
		PostResponse postResponse = postService.savePost(postRequest);

		URI location = ServletUriComponentsBuilder
			.fromCurrentContextPath().path("/posts/")
			.buildAndExpand(postResponse.getPostId()).toUri();

		return ResponseEntity.created(location).body(postResponse);
	}

	@PutMapping("/posts/{postId}")
	public ResponseEntity<PostResponse> update(@PathVariable Long postId,
		@RequestBody PostRequest postRequest) {
		return ResponseEntity.ok(postService.updatePost(postId, postRequest));
	}

	@DeleteMapping("/posts/{postId}")
	public ResponseEntity<Void> delete(@PathVariable Long postId) {
		postService.deletePost(postId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostResponse> read(@PathVariable Long postId) {
		return ResponseEntity.ok(postService.readPost(postId));
	}

	@GetMapping("/posts")
	public ResponseEntity<List<PostListResponse>> readPostList(Pageable pageable) {
		return ResponseEntity.ok(postService.readPosts(pageable));
	}

	@GetMapping("/posts/search")
	public ResponseEntity<List<PostListResponse>> search(@RequestParam(value = "t") String searchType,
		@RequestParam(value = "q") String query, Pageable pageable) {
		return ResponseEntity.ok(postService.search(searchType, query, pageable));
	}

	@PostMapping("/posts/image")
	public ResponseEntity<String> changeUserProfileImage(@RequestParam final Long userId,
		@RequestPart("image") final MultipartFile multipartFile) {
		return ResponseEntity.ok(s3UploadUtil.uploadPostImage(multipartFile, userId));
	}
}
