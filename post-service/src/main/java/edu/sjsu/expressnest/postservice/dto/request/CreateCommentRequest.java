package edu.sjsu.expressnest.postservice.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateCommentRequest {
	private String commentText;
	private long userId;
	private long postId;
}
