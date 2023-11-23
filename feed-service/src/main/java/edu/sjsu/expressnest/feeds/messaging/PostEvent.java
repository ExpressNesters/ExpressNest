package edu.sjsu.expressnest.feeds.messaging;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostEvent {

	private long postId;
	
	private long actionedBy;
	
	@JsonProperty("postEventType")
	private String type;
}
