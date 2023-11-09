package edu.sjsu.expressnest.postservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.expressnest.postservice.dto.CommentDTO;
import edu.sjsu.expressnest.postservice.exception.ResourceNotFoundException;
import edu.sjsu.expressnest.postservice.service.CommentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/comment")
@Validated
public class CommentController {
	
	@Autowired
	CommentService commentService;
	
	@GetMapping("/getCommentByCommentId")
	public ResponseEntity<CommentDTO> getCommentByCommentId(@NotNull @Positive @RequestParam long commentId) throws ResourceNotFoundException {
		return null;
	}
	
	@GetMapping("/getCommentsByPostId")
	public ResponseEntity<List<CommentDTO>> getCommentsByPostId(@NotNull @Positive @RequestParam long postId) throws ResourceNotFoundException {
		return null;
	}
	                
	@GetMapping("/getCommentsByUserId")
	public ResponseEntity<List<CommentDTO>> getCommentsByUserId(@NotNull @RequestParam long userId) {
		return null;
	}
	
	@PostMapping("/createComment")
	public ResponseEntity<CommentDTO> createComment(@Valid @RequestBody CommentDTO commentDTO) {
		return null;
	}
	
	@PostMapping("/updateComment")
	public ResponseEntity<CommentDTO> updatePost(@RequestBody CommentDTO commentDTO) {
		return null;
	}
	
	@PostMapping("/deleteComment")
	public ResponseEntity<String> deleteComment(@NotNull @Positive @RequestBody long commentId) throws ResourceNotFoundException {
		return null;
	}

}
