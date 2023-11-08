package edu.sjsu.expressnest.postservice.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String resourceId;
	private String message;
	
	public ResourceNotFoundException(String message, String resourceId) {
		super(message);
		this.message = message;
		this.resourceId = resourceId;
	}

}
