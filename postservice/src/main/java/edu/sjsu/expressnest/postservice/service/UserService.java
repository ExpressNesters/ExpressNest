package edu.sjsu.expressnest.postservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.expressnest.postservice.exception.ResourceNotFoundException;
import edu.sjsu.expressnest.postservice.model.User;
import edu.sjsu.expressnest.postservice.repository.UserRepository;
import edu.sjsu.expressnest.postservice.util.PostServiceConstants;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MessageService messageService;
	
	public User getUserByUserId(long userId) throws ResourceNotFoundException {
		User retrievedUser = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(
						messageService.getMessage(PostServiceConstants.USER_NOT_FOUND_ERROR_KEY, userId),
						String.valueOf(userId)));
		return retrievedUser;
	}

}
