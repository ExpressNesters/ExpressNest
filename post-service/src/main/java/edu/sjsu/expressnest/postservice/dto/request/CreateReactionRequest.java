package edu.sjsu.expressnest.postservice.dto.request;

import edu.sjsu.expressnest.postservice.util.ReactionType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateReactionRequest {

	private ReactionType reactionType;
	private long userId;
	private long postId;
}
