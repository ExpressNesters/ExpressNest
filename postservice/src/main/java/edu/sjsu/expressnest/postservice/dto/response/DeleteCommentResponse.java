package edu.sjsu.expressnest.postservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteCommentResponse {
	
	private long commentId;
	private String message;

}
