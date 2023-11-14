package edu.sjsu.expressnest.postservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteReactionResponse {
	
	private long reactionId;
	private String message;

}
