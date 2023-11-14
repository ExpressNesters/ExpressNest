package edu.sjsu.expressnest.postservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeletePostResponse {
		
	private long postId;
	private String message;

}
