package edu.sjsu.expressnest.feeds.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class FeedServiceExceptionHandler extends ResponseEntityExceptionHandler {

		@ExceptionHandler(ResourceNotFoundException.class)
		public ResponseEntity<FeedServiceError> handleResourceNotFoundException(ResourceNotFoundException exception) {
			
			FeedServiceError feedServiceError = FeedServiceError.builder()
					.date(new Date())
					.message(exception.getMessage())
					.build();
	    	return new ResponseEntity<FeedServiceError>(feedServiceError, HttpStatus.NOT_FOUND);
		}
		
		@ExceptionHandler(Exception.class)
		public ResponseEntity<FeedServiceError> handleException(Exception exception) {
			exception.printStackTrace();
			FeedServiceError feedServiceError = FeedServiceError.builder()
					.date(new Date())
					.message(exception.getMessage())
					.build();
	    	return new ResponseEntity<FeedServiceError>(feedServiceError, HttpStatus.INTERNAL_SERVER_ERROR);
		}
}
