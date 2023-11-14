package edu.sjsu.expressnest.postservice.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCommentRequest {
	
	private String commentText;
}
