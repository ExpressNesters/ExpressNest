package edu.sjsu.expressnest.postservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.expressnest.postservice.dto.request.CreateCommentRequest;
import edu.sjsu.expressnest.postservice.dto.request.UpdateCommentRequest;
import edu.sjsu.expressnest.postservice.dto.response.CreateCommentResponse;
import edu.sjsu.expressnest.postservice.dto.response.DeleteCommentResponse;
import edu.sjsu.expressnest.postservice.dto.response.GetCommentResponse;
import edu.sjsu.expressnest.postservice.dto.response.GetCommentsResponse;
import edu.sjsu.expressnest.postservice.dto.response.UpdateCommentResponse;
import edu.sjsu.expressnest.postservice.exception.ResourceNotFoundException;
import edu.sjsu.expressnest.postservice.service.CommentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/comments")
@Validated
public class CommentController {

	@Autowired
	CommentService commentService;

	@GetMapping("/{commentId}")
	public ResponseEntity<GetCommentResponse> getCommentByCommentId(@NotNull @Positive @PathVariable long commentId)
			throws ResourceNotFoundException {
		GetCommentResponse getCommentResponse = commentService.getCommentByCommentId(commentId);
		return new ResponseEntity<>(getCommentResponse, HttpStatus.OK);
	}

	@GetMapping("/post/{postId}")
	public ResponseEntity<GetCommentsResponse> getCommentsByPostId(@NotNull @Positive @PathVariable long postId,
			@PageableDefault(page = 0, size = 10, sort = "createdAt,desc") Pageable pageable)
			throws ResourceNotFoundException {
		GetCommentsResponse getCommentsResponse = commentService.getCommentsByPostId(postId, pageable);
		return new ResponseEntity<>(getCommentsResponse, HttpStatus.OK);
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<GetCommentsResponse> getCommentsByUserId(@NotNull @PathVariable long userId,
			@PageableDefault(page = 0, size = 10, sort = "createdAt,desc") Pageable pageable) {
		GetCommentsResponse getCommentsResponse = commentService.getCommentsByUserId(userId, pageable);
		return new ResponseEntity<>(getCommentsResponse, HttpStatus.OK);
	}

	@PostMapping("/")
	public ResponseEntity<CreateCommentResponse> createComment(
			@Valid @RequestBody CreateCommentRequest createCommentRequest) throws ResourceNotFoundException {
		CreateCommentResponse createCommentResponse = commentService.createComment(createCommentRequest);
		return new ResponseEntity<>(createCommentResponse, HttpStatus.CREATED);
	}

	@PutMapping("/{commentId}")
	public ResponseEntity<UpdateCommentResponse> updateComment(@PathVariable long commentId,
			@RequestBody UpdateCommentRequest updateCommentRequest) throws ResourceNotFoundException {
		UpdateCommentResponse updateCommentResponse = commentService.updateComment(commentId, updateCommentRequest);
		return new ResponseEntity<>(updateCommentResponse, HttpStatus.OK);
	}

	@DeleteMapping("/{commentId}")
	public ResponseEntity<DeleteCommentResponse> deleteComment(@NotNull @Positive @PathVariable long commentId)
			throws ResourceNotFoundException {
		DeleteCommentResponse deleteCommentResponse = commentService.deleteComment(commentId);
		return new ResponseEntity<>(deleteCommentResponse, HttpStatus.OK);
	}

}
