package edu.sjsu.expressnest.postservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.expressnest.postservice.dto.PostDTO;
import edu.sjsu.expressnest.postservice.service.PostService;

@RestController
@RequestMapping("/post")
public class PostServiceController {
	
	@Autowired
	PostService postService;
	
	@GetMapping("/greeting")
	public String greeting(@RequestParam String name) {
		return "Hello " + name;
	}
	
	@GetMapping("/getPostByPostId")
	public ResponseEntity<PostDTO> getPostByPostId(@RequestParam long postId) {
		return null;
	}
	
	@GetMapping("/getPostsByUserId")
	public ResponseEntity<PostDTO> getPostsByUserId(@RequestParam long userId) {
		return null;
	}
	
	@GetMapping("/getPostsByPostIds")
	public ResponseEntity<PostDTO> getPostsByPostIds(@RequestParam List<Long> postId) {
		return null;
	}
	
	@PostMapping("/createPost")
	public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {
		PostDTO createdPostDTO = postService.createPost(postDTO);
		return new ResponseEntity<>(createdPostDTO, HttpStatus.CREATED);
	}
	
	@PostMapping("/updatePost")
	public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO postDTO) {
		return null;
	}
	
	@PostMapping("/deletePost")
	public ResponseEntity<PostDTO> deletePost(@RequestBody long postId) {
		return null;
	}

}
