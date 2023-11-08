package edu.sjsu.expressnest.postservice.mapper.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import edu.sjsu.expressnest.postservice.dto.ReactionDTO;
import edu.sjsu.expressnest.postservice.mapper.ReactionMapper;
import edu.sjsu.expressnest.postservice.model.Reaction;

@Component
public class ReactionMapperImpl implements ReactionMapper{

	@Override
	public Reaction toReaction(ReactionDTO reactionDTO) {
		if (reactionDTO == null) {
			return null;
		}
		return Reaction.builder()
				.reactionId(reactionDTO.getReactionId())
				.reactionType(reactionDTO.getReactionType())
				.userId(reactionDTO.getUserId())
				.build();
	}
	
	@Override
	public ReactionDTO toReactionDTO(Reaction reaction) {
		if (reaction == null) {
			return null;
		}
		return ReactionDTO.builder()
				.reactionId(reaction.getReactionId())
				.reactionType(reaction.getReactionType())
				.userId(reaction.getUserId())
				.createdAt(reaction.getCreatedAt())
				.updatedAt(reaction.getUpdatedAt())
				.deletedAt(reaction.getDeletedAt())
				.build();
	}

	@Override
	public List<ReactionDTO> toReactionDTOs(List<Reaction> reactions) {
		if (reactions == null) {
			return Collections.emptyList();
		}
		return reactions.stream()
				.map(this::toReactionDTO)
				.collect(Collectors.toList());
	}

}
