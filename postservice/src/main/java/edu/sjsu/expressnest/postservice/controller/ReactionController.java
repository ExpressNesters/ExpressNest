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

import edu.sjsu.expressnest.postservice.dto.ReactionDTO;
import edu.sjsu.expressnest.postservice.exception.ResourceNotFoundException;
import edu.sjsu.expressnest.postservice.service.ReactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/reaction")
@Validated
public class ReactionController {
	
	@Autowired
	ReactionService reactionService;
	
	@GetMapping("/getReactionByReactionId")
	public ResponseEntity<ReactionDTO> getReactionByReactionId(@NotNull @Positive @RequestParam long reactionId) throws ResourceNotFoundException {
		return null;
	}
	
	@GetMapping("/getReactionsByPostId")
	public ResponseEntity<List<ReactionDTO>> getReactionsByPostId(@NotNull @Positive @RequestParam long postId) throws ResourceNotFoundException {
		return null;
	}
	
	@PostMapping("/createReaction")
	public ResponseEntity<ReactionDTO> createReaction(@Valid @RequestBody ReactionDTO reactionDTO) {
		return null;
	}
	
	@PostMapping("/updateReaction")
	public ResponseEntity<ReactionDTO> updateReaction(@RequestBody ReactionDTO reactionDTO) {
		return null;
	}
	
	@PostMapping("/deleteReaction")
	public ResponseEntity<String> deleteReaction(@NotNull @Positive @RequestBody long reactionId) throws ResourceNotFoundException {
		return null;
	}

}
