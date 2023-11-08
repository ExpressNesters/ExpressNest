package edu.sjsu.expressnest.postservice.mapper;

import java.util.List;

import edu.sjsu.expressnest.postservice.dto.ReactionDTO;
import edu.sjsu.expressnest.postservice.model.Reaction;

public interface ReactionMapper {
	
	Reaction toReaction(ReactionDTO reactionDTO);
	
	ReactionDTO toReactionDTO(Reaction reaction);
	
	List<ReactionDTO> toReactionDTOs(List<Reaction> reactions);

}
