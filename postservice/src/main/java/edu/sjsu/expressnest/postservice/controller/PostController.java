package edu.sjsu.expressnest.postservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.expressnest.postservice.dto.PostDTO;
import edu.sjsu.expressnest.postservice.exception.ResourceNotFoundException;
import edu.sjsu.expressnest.postservice.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/post")
@Validated
public class PostController {
	
	@Autowired
	PostService postService;
	
	@GetMapping("/getPostByPostId")
	public ResponseEntity<PostDTO> getPostByPostId(@NotNull @Positive @RequestParam long postId) throws ResourceNotFoundException {
		PostDTO retrievedPost = postService.getPostByPostId(postId);
		return new ResponseEntity<PostDTO>(retrievedPost, HttpStatus.OK);
	}
	
	@GetMapping("/getPostsByUserId")
	public ResponseEntity<List<PostDTO>> getPostsByUserId(@NotNull @Positive @RequestParam long userId) throws ResourceNotFoundException {
		List<PostDTO> retrievedPosts = postService.getPostsByUserId(userId);
		return new ResponseEntity<List<PostDTO>>(retrievedPosts, HttpStatus.OK);
	}
	                
	@GetMapping("/getPostsByPostIds")
	public ResponseEntity<List<PostDTO>> getPostsByPostIds(@NotNull @RequestParam List<Long> postIds) {
		List<PostDTO> retrievedPosts = postService.getPostsByPostIds(postIds);
		return new ResponseEntity<List<PostDTO>>(retrievedPosts, HttpStatus.OK);
	}
	
	@PostMapping("/createPost")
	public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostDTO postDTO) {
		PostDTO createdPostDTO = postService.createPost(postDTO);
		return new ResponseEntity<PostDTO>(createdPostDTO, HttpStatus.CREATED);
	}
	
	@PostMapping("/updatePost")
	public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO postDTO) {
		return null;
	}
	
	@PostMapping("/deletePost")
	public ResponseEntity<String> deletePost(@NotNull @Positive @RequestBody long postId) throws ResourceNotFoundException {
		String postDeleteMessage = postService.deletePost(postId);
		return new ResponseEntity<String>(postDeleteMessage, HttpStatus.OK);
	}

}
