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

import edu.sjsu.expressnest.postservice.dto.request.CreateReactionRequest;
import edu.sjsu.expressnest.postservice.dto.request.UpdateReactionRequest;
import edu.sjsu.expressnest.postservice.dto.response.CreateReactionResponse;
import edu.sjsu.expressnest.postservice.dto.response.DeleteReactionResponse;
import edu.sjsu.expressnest.postservice.dto.response.GetReactionResponse;
import edu.sjsu.expressnest.postservice.dto.response.GetReactionsResponse;
import edu.sjsu.expressnest.postservice.dto.response.UpdateReactionResponse;
import edu.sjsu.expressnest.postservice.exception.ResourceNotFoundException;
import edu.sjsu.expressnest.postservice.service.ReactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/reactions")
@Validated
public class ReactionController {

	@Autowired
	ReactionService reactionService;

	@GetMapping("/{reactionId}")
	public ResponseEntity<GetReactionResponse> getReactionByReactionId(@NotNull @Positive @PathVariable long reactionId)
			throws ResourceNotFoundException {
		GetReactionResponse getReactionResponse = reactionService.getReactionByReactionId(reactionId);
		return new ResponseEntity<>(getReactionResponse, HttpStatus.OK);
	}

	@GetMapping("/post/{postId}")
	public ResponseEntity<GetReactionsResponse> getReactionsByPostId(@NotNull @Positive @PathVariable long postId,
			@PageableDefault(page = 0, size = 10, sort = "createdAt,desc") Pageable pageable)
			throws ResourceNotFoundException {
		GetReactionsResponse getReactionsResponse = reactionService.getReactionsByPostId(postId, pageable);
		return new ResponseEntity<>(getReactionsResponse, HttpStatus.OK);
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<GetReactionsResponse> getReactionsByUserId(@NotNull @PathVariable long userId,
			@PageableDefault(page = 0, size = 10, sort = "createdAt,desc") Pageable pageable) {
		GetReactionsResponse getReactionsResponse = reactionService.getReactionsByUserId(userId, pageable);
		return new ResponseEntity<>(getReactionsResponse, HttpStatus.OK);
	}

	@PostMapping("/")
	public ResponseEntity<CreateReactionResponse> createReaction(
			@Valid @RequestBody CreateReactionRequest createReactionRequest) throws ResourceNotFoundException {
		CreateReactionResponse createReactionResponse = reactionService.createReaction(createReactionRequest);
		return new ResponseEntity<>(createReactionResponse, HttpStatus.CREATED);
	}

	@PutMapping("/{reactionId}")
	public ResponseEntity<UpdateReactionResponse> updateReaction(@PathVariable long reactionId,
			@RequestBody UpdateReactionRequest updateReactionRequest) throws ResourceNotFoundException {
		UpdateReactionResponse updateReactionResponse = reactionService.updateReaction(reactionId, updateReactionRequest);
		return new ResponseEntity<>(updateReactionResponse, HttpStatus.OK);
	}

	@DeleteMapping("/{reactionId}")
	public ResponseEntity<DeleteReactionResponse> deleteReaction(@NotNull @Positive @PathVariable long reactionId)
			throws ResourceNotFoundException {
		DeleteReactionResponse deleteReactionResponse = reactionService.deleteReaction(reactionId);
		return new ResponseEntity<>(deleteReactionResponse, HttpStatus.OK);
	}

}
