package edu.sjsu.expressnest.postservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.sjsu.expressnest.postservice.dto.request.CreateReactionRequest;
import edu.sjsu.expressnest.postservice.dto.request.UpdateReactionRequest;
import edu.sjsu.expressnest.postservice.dto.response.CreateReactionResponse;
import edu.sjsu.expressnest.postservice.dto.response.DeleteReactionResponse;
import edu.sjsu.expressnest.postservice.dto.response.GetReactionResponse;
import edu.sjsu.expressnest.postservice.dto.response.GetReactionsResponse;
import edu.sjsu.expressnest.postservice.dto.response.UpdateReactionResponse;
import edu.sjsu.expressnest.postservice.exception.ResourceNotFoundException;
import edu.sjsu.expressnest.postservice.mapper.impl.ReactionMapper;
import edu.sjsu.expressnest.postservice.model.Post;
import edu.sjsu.expressnest.postservice.model.Reaction;
import edu.sjsu.expressnest.postservice.repository.PostRepository;
import edu.sjsu.expressnest.postservice.repository.ReactionRepository;
import edu.sjsu.expressnest.postservice.util.PostServiceConstants;
import jakarta.transaction.Transactional;

@Service
public class ReactionService {

	@Autowired
	private ReactionMapper reactionMapper;

	@Autowired
	private ReactionRepository reactionRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private MessageService messageService;

	@Transactional
	public CreateReactionResponse createReaction(CreateReactionRequest createReactionRequest) throws ResourceNotFoundException {
		long postId = createReactionRequest.getPostId();
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException(
						messageService.getMessage(PostServiceConstants.POST_NOT_FOUND_ERROR_KEY, postId)));
		int currentTotalReactions = post.getTotalNoOfReactions();
		post.setTotalNoOfReactions(currentTotalReactions + 1);
		Reaction reaction = reactionMapper.toReaction(createReactionRequest);
		reaction.setPost(post);
		Reaction createdReaction = reactionRepository.save(reaction);
		postRepository.save(post);
		return reactionMapper.toCreateReactionResponse(createdReaction);
	}
	
	@Transactional
	public UpdateReactionResponse updateReaction(long reactionId, UpdateReactionRequest updateReactionRequest) throws ResourceNotFoundException {
		Reaction reaction = reactionRepository.findById(reactionId)
				.orElseThrow(() -> new ResourceNotFoundException(
						messageService.getMessage(PostServiceConstants.REACTION_NOT_FOUND_ERROR_KEY, reactionId)));
		reactionMapper.mapUpdateReactionRequestToReaction(updateReactionRequest, reaction);
		Reaction updatedReaction = reactionRepository.save(reaction);
		return reactionMapper.toUpdateReactionResponse(updatedReaction);
	}
	
	@Transactional
	public DeleteReactionResponse deleteReaction(long reactionId) throws ResourceNotFoundException {

		Reaction reaction = reactionRepository.findById(reactionId)
		.orElseThrow(() -> new ResourceNotFoundException(
				messageService.getMessage(PostServiceConstants.REACTION_NOT_FOUND_ERROR_KEY, reactionId)));
		
		Post post = reaction.getPost();
		int currentTotalReactions = post.getTotalNoOfReactions();
		reactionRepository.delete(reaction);
		post.setTotalNoOfReactions(Math.max(0, currentTotalReactions - 1));
		postRepository.save(post);
		
		return reactionMapper.toDeleteReactionResponse(reactionId);
	}
	
	public GetReactionResponse getReactionByReactionId(long reactionId) throws ResourceNotFoundException {
		return reactionRepository.findById(reactionId)
				.map(reactionMapper::toGetReactionResponse)
				.orElseThrow(() -> new ResourceNotFoundException(
						messageService.getMessage(PostServiceConstants.REACTION_NOT_FOUND_ERROR_KEY, reactionId)));
	}

	public GetReactionsResponse getReactionsByPostId(long postId, Pageable pageable) {
		Page<Reaction> reactionsPage = reactionRepository.findByPost_PostId(postId, pageable);
		return reactionMapper.toGetReactionsResponse(reactionsPage);
	}

	public GetReactionsResponse getReactionsByUserId(long userId, Pageable pageable) {
		Page<Reaction> reactionsPage = reactionRepository.findByUserId(userId, pageable);
		return reactionMapper.toGetReactionsResponse(reactionsPage);
	}

}
