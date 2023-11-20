package edu.sjsu.expressnest.postservice.dto;

import java.util.Date;

import edu.sjsu.expressnest.postservice.util.ReactionType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReactionDTO {
	
	private long reactionId;
	private ReactionType reactionType;
	private long userId;
	private long postId;
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;
}
