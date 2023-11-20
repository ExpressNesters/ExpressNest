package edu.sjsu.expressnest.postservice.messaging;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostMessage {
	
	private long postId;
	private long userId;

}
