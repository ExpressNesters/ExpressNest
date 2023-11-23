package edu.sjsu.expressnest.postservice.mapper.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import edu.sjsu.expressnest.postservice.dto.ReactionDTO;
import edu.sjsu.expressnest.postservice.dto.request.CreateReactionRequest;
import edu.sjsu.expressnest.postservice.dto.request.UpdateReactionRequest;
import edu.sjsu.expressnest.postservice.dto.response.CreateReactionResponse;
import edu.sjsu.expressnest.postservice.dto.response.DeleteReactionResponse;
import edu.sjsu.expressnest.postservice.dto.response.GetReactionResponse;
import edu.sjsu.expressnest.postservice.dto.response.GetReactionsResponse;
import edu.sjsu.expressnest.postservice.dto.response.UpdateReactionResponse;
import edu.sjsu.expressnest.postservice.model.Reaction;
import edu.sjsu.expressnest.postservice.service.MessageService;
import edu.sjsu.expressnest.postservice.util.PostServiceConstants;

@Component
public class ReactionMapper {
	
	@Autowired
	private MessageService messageService;
	
	public Reaction toReaction(CreateReactionRequest createReactionRequest) {
		//set the Post in the service layer
		return Reaction.builder()
				.reactionType(createReactionRequest.getReactionType())
				.userId(createReactionRequest.getUserId())
				.build();
	}
	
	public CreateReactionResponse toCreateReactionResponse(Reaction reaction) {
		return CreateReactionResponse.builder()
				.createdReactionDTO(toReactionDTO(reaction))
				.build();
	}
	
	public void mapUpdateReactionRequestToReaction(UpdateReactionRequest updateReactionRequest, Reaction reaction) {
		if (updateReactionRequest !=null && reaction != null) {
			reaction.setReactionType(updateReactionRequest.getReactionType());
		}
	}
	
	public UpdateReactionResponse toUpdateReactionResponse(Reaction reaction) {
		return UpdateReactionResponse.builder()
				.updatedReactionDTO(toReactionDTO(reaction))
				.build();
	}
	
	public GetReactionsResponse toGetReactionsResponse(Page<Reaction> reactionsPage) {
		return GetReactionsResponse.builder()
				.reactionDTOs(toReactionDTOs(reactionsPage))
				.currentPage(reactionsPage.getNumber())
				.pageSize(reactionsPage.getSize())
				.totalItems(reactionsPage.getTotalElements())
                .totalPages(reactionsPage.getTotalPages())
				.build();
	}
	
	public GetReactionResponse toGetReactionResponse(Reaction reaction) {
		return GetReactionResponse.builder()
				.reactionDTO(toReactionDTO(reaction))
				.build();
	}
	
	public DeleteReactionResponse toDeleteReactionResponse(long reactionId) {
		return DeleteReactionResponse.builder()
				.reactionId(reactionId)
				.message(messageService.getMessage(PostServiceConstants.COMMENT_DELETED_INFO_KEY, reactionId))
				.build();
	}
	
	private List<ReactionDTO> toReactionDTOs(Page<Reaction> reactionsPage) {
		return reactionsPage.getContent().stream()
				.map(this::toReactionDTO)
				.collect(Collectors.toList());
	}
	
	private ReactionDTO toReactionDTO(Reaction reaction) {
		return ReactionDTO.builder()
				.reactionId(reaction.getReactionId())
				.reactionType(reaction.getReactionType())
				.userId(reaction.getUserId())
				.postId(reaction.getPost().getPostId())
				.createdAt(reaction.getCreatedAt())
				.updatedAt(reaction.getUpdatedAt())
				.deletedAt(reaction.getDeletedAt())
				.build();
	}

}
