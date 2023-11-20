package edu.sjsu.expressnest.postservice.dto.response;

import edu.sjsu.expressnest.postservice.dto.ReactionDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateReactionResponse {
	
	private ReactionDTO createdReactionDTO;

}
