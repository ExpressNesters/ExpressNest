package edu.sjsu.expressnest.postservice.exception;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import edu.sjsu.expressnest.postservice.service.MessageService;
import edu.sjsu.expressnest.postservice.util.PostServiceConstants;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class PostServiceExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	private MessageService messageService;
	
		@ExceptionHandler(ConstraintViolationException.class)
	    public ResponseEntity<PostServiceError> handleConstraintViolationException(ConstraintViolationException exception) {
	    	Map<String, String> errors = exception.getConstraintViolations().stream()
	                .collect(Collectors.toMap(
	                        violation -> violation.getPropertyPath().toString(),
	                        violation -> violation.getMessage()
	                ));
	    	
	    	PostServiceError postServiceError = PostServiceError.builder()
					.date(new Date())
					.message(messageService.getMessage(PostServiceConstants.VALIDATION_ERROR_KEY))
					.errors(errors)
					.build();
	    	return new ResponseEntity<PostServiceError>(postServiceError, HttpStatus.BAD_REQUEST);
	    	
	    }

		@ExceptionHandler(ResourceNotFoundException.class)
		public ResponseEntity<PostServiceError> handleResourceNotFoundException(ResourceNotFoundException exception) {
			
			PostServiceError postServiceError = PostServiceError.builder()
					.date(new Date())
					.message(exception.getMessage())
					.build();
	    	return new ResponseEntity<PostServiceError>(postServiceError, HttpStatus.NOT_FOUND);
		}
		
		@ExceptionHandler(Exception.class)
		public ResponseEntity<PostServiceError> handleException(Exception exception) {
			exception.printStackTrace();
			PostServiceError postServiceError = PostServiceError.builder()
					.date(new Date())
					.message(exception.getMessage())
					.build();
	    	return new ResponseEntity<PostServiceError>(postServiceError, HttpStatus.INTERNAL_SERVER_ERROR);
		}
}
