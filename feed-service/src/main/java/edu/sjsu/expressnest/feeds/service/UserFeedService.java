package edu.sjsu.expressnest.feeds.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.expressnest.feeds.exception.ResourceNotFoundException;
import edu.sjsu.expressnest.feeds.model.UserFeeds;
import edu.sjsu.expressnest.feeds.repository.UserFeedsRepository;

@Service
public class UserFeedService {
	
	@Autowired
	private UserFeedsRepository userFeedsRepository;

	public UserFeeds getFeedsForUser(Long userId) throws ResourceNotFoundException {
		return userFeedsRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("UserId does not exist"));
	}
}
