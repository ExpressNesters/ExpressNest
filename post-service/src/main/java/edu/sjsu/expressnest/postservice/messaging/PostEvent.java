package edu.sjsu.expressnest.postservice.messaging;

import edu.sjsu.expressnest.postservice.util.PostEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostEvent {
	
	private long postId;
	private long actionedBy;
	private PostEventType postEventType;

}
