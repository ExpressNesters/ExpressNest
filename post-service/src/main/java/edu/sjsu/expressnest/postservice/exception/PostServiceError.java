package edu.sjsu.expressnest.postservice.exception;

import java.util.Date;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostServiceError {
	
	private Date date;
	private String message;
	private Map<String, String> errors;

}
