package edu.sjsu.expressnest.postservice.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

import edu.sjsu.expressnest.postservice.dto.request.CreatePostRequest;
import edu.sjsu.expressnest.postservice.dto.request.UpdatePostRequest;
import edu.sjsu.expressnest.postservice.dto.response.CreatePostResponse;
import edu.sjsu.expressnest.postservice.dto.response.DeletePostResponse;
import edu.sjsu.expressnest.postservice.dto.response.GetPostResponse;
import edu.sjsu.expressnest.postservice.dto.response.GetPostsResponse;
import edu.sjsu.expressnest.postservice.dto.response.UpdatePostResponse;
import edu.sjsu.expressnest.postservice.exception.ResourceNotFoundException;
import edu.sjsu.expressnest.postservice.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/posts")
@Validated
public class PostController {
	
	@Autowired
	PostService postService;
	
	@GetMapping("/{postId}")
	public ResponseEntity<GetPostResponse> getPostByPostId(@NotNull @Positive @PathVariable long postId) throws ResourceNotFoundException {
		GetPostResponse retrievedPost = postService.getPostByPostId(postId);
		return new ResponseEntity<>(retrievedPost, HttpStatus.OK);
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<GetPostsResponse> getPostsByUserId(@NotNull @Positive @PathVariable long userId,
			@PageableDefault(page = 0, size = 10, sort = "createdAt,desc") Pageable pageable) {
		GetPostsResponse retrievedPosts = postService.getPostsByUserId(userId, pageable);
		return new ResponseEntity<>(retrievedPosts, HttpStatus.OK);
	}
	                
	@GetMapping("/by-postIds")
	public ResponseEntity<GetPostsResponse> getPostsByPostIds(@NotNull @RequestParam List<Long> postIds,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {
		GetPostsResponse retrievedPosts = postService.getPostsByPostIds(postIds, pageable);
		return new ResponseEntity<>(retrievedPosts, HttpStatus.OK);
	}
	
	@PostMapping("/withoutAttachment")
	public ResponseEntity<CreatePostResponse> createPost(@Valid @RequestBody CreatePostRequest createPostRequest) {
		CreatePostResponse createdPostDTO = postService.createPost(createPostRequest);
		return new ResponseEntity<>(createdPostDTO, HttpStatus.CREATED);
	}
	
	@PostMapping("/")
	public ResponseEntity<CreatePostResponse> createPostWithAttachment(@Valid @RequestPart("post") CreatePostRequest createPostRequest,
			@RequestPart("file") MultipartFile file) throws IOException {
		CreatePostResponse createdPostDTO = postService.createPostWithAttachment(createPostRequest, file);
		return new ResponseEntity<>(createdPostDTO, HttpStatus.CREATED);
	}
	
	@PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<String> uploadFile(@Valid @RequestPart("post") CreatePostRequest createPostRequest, 
			@RequestPart MultipartFile file) throws IOException {
		postService.createPostWithAttachment(createPostRequest, file);
		return new ResponseEntity<>("uploaded", HttpStatus.OK);
	}
	
	@PutMapping("/{postId}")
	public ResponseEntity<UpdatePostResponse> updatePost(@PathVariable long postId, @RequestBody UpdatePostRequest updatePostRequest) throws ResourceNotFoundException {
		updatePostRequest.setPostId(postId);
		UpdatePostResponse updatedPostDTO = postService.updatePost(updatePostRequest);
		return new ResponseEntity<>(updatedPostDTO, HttpStatus.OK);
	}
	
	@DeleteMapping("/{postId}")
	public ResponseEntity<DeletePostResponse> deletePost(@NotNull @Positive @PathVariable long postId) throws ResourceNotFoundException {
		DeletePostResponse deletePostResponse = postService.deletePost(postId);
		return new ResponseEntity<>(deletePostResponse, HttpStatus.OK);
	}

}
