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
import org.springframework.web.bind.annotation.CrossOrigin;
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
import lombok.extern.log4j.Log4j2;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/posts")
@Validated
@Log4j2
public class PostController {
	
	@Autowired
	PostService postService;
	
	@GetMapping("/{postId}")
	public ResponseEntity<GetPostResponse> getPostByPostId(@NotNull @Positive @PathVariable long postId) throws ResourceNotFoundException {
		log.info("GetPostByPostId called with postId={}", postId);
		GetPostResponse retrievedPost = postService.getPostByPostId(postId);
		log.info("GetPostByPostId response={}", retrievedPost);
		return new ResponseEntity<>(retrievedPost, HttpStatus.OK);
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<GetPostsResponse> getPostsByUserId(@NotNull @Positive @PathVariable long userId,
			@PageableDefault(page = 0, size = 10, sort = "createdAt,desc") Pageable pageable) {
		log.info("getPostsByUserId called with userId={}", userId);
		GetPostsResponse retrievedPosts = postService.getPostsByUserId(userId, pageable);
		log.info("getPostsByUserId TotalPosts={}", retrievedPosts.getTotalItems());
		return new ResponseEntity<>(retrievedPosts, HttpStatus.OK);
	}
	                
	@GetMapping("/by-postIds")
	public ResponseEntity<GetPostsResponse> getPostsByPostIds(@NotNull @RequestParam List<Long> postIds,
			@PageableDefault(page = 0, size = 10, sort = "createdAt,desc") Pageable pageable) {
		log.info("getPostsByPostIds called with postIds={}", postIds);
		GetPostsResponse retrievedPosts = postService.getPostsByPostIds(postIds, pageable);
		log.info("getPostsByPostIds End");
		return new ResponseEntity<>(retrievedPosts, HttpStatus.OK);
	}
	
	@PostMapping("/withoutAttachment")
	public ResponseEntity<CreatePostResponse> createPost(@Valid @RequestBody CreatePostRequest createPostRequest) {
		log.info("Create Post Request");
		CreatePostResponse createdPostDTO = postService.createPost(createPostRequest);
		log.info("Post Created with Id={}", createdPostDTO.getCreatedPostDTO().getPostId());
		return new ResponseEntity<>(createdPostDTO, HttpStatus.CREATED);
	}
	
	@PostMapping(path = "/", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<String> uploadFile(@Valid @RequestPart CreatePostRequest createPostRequest, 
			@RequestPart  MultipartFile file) throws IOException {
		log.info("Create Post Request with attachment");
		postService.createPostWithAttachment(createPostRequest, file);
		log.info("Post created with attachment");
		return new ResponseEntity<>("uploaded", HttpStatus.OK);
	}
	
	@PutMapping("/{postId}")
	public ResponseEntity<UpdatePostResponse> updatePost(@PathVariable long postId, @RequestBody UpdatePostRequest updatePostRequest) throws ResourceNotFoundException {
		log.info("Update Post request with postId={}", postId);
		UpdatePostResponse updatedPostDTO = postService.updatePost(postId, updatePostRequest);
		log.info("Post updated postId={}", postId);
		return new ResponseEntity<>(updatedPostDTO, HttpStatus.OK);
	}
	
	@DeleteMapping("/{postId}")
	public ResponseEntity<DeletePostResponse> deletePost(@NotNull @Positive @PathVariable long postId) throws ResourceNotFoundException {
		log.info("Delete Post with postId={}", postId);
		DeletePostResponse deletePostResponse = postService.deletePost(postId);
		log.info("Post deleted with postId={}", postId);
		return new ResponseEntity<>(deletePostResponse, HttpStatus.OK);
	}

}
