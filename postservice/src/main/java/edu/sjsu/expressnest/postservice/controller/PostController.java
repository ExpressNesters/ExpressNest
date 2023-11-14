package edu.sjsu.expressnest.postservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.expressnest.postservice.dto.request.CreatePostRequest;
import edu.sjsu.expressnest.postservice.dto.request.UpdatePostRequest;
import edu.sjsu.expressnest.postservice.dto.response.CreatePostResponse;
import edu.sjsu.expressnest.postservice.dto.response.DeletePostResponse;
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
	public ResponseEntity<GetPostsResponse> getPostByPostId(@NotNull @Positive @PathVariable long postId) throws ResourceNotFoundException {
		GetPostsResponse retrievedPost = postService.getPostByPostId(postId);
		return new ResponseEntity<>(retrievedPost, HttpStatus.OK);
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<GetPostsResponse> getPostsByUserId(@NotNull @Positive @PathVariable long userId) {
		GetPostsResponse retrievedPosts = postService.getPostsByUserId(userId);
		return new ResponseEntity<>(retrievedPosts, HttpStatus.OK);
	}
	                
	@GetMapping("/by-postIds")
	public ResponseEntity<GetPostsResponse> getPostsByPostIds(@NotNull @RequestParam List<Long> postIds) {
		GetPostsResponse retrievedPosts = postService.getPostsByPostIds(postIds);
		return new ResponseEntity<>(retrievedPosts, HttpStatus.OK);
	}
	
	@PostMapping("/")
	public ResponseEntity<CreatePostResponse> createPost(@Valid @RequestBody CreatePostRequest createPostRequest) {
		CreatePostResponse createdPostDTO = postService.createPost(createPostRequest);
		return new ResponseEntity<>(createdPostDTO, HttpStatus.CREATED);
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
