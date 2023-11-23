package edu.sjsu.expressnest.feeds.exception;

import java.util.Date;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedServiceError {
	
	private Date date;
	private String message;
	private Map<String, String> errors;

}