package edu.sjsu.expressnest.feeds.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.expressnest.feeds.exception.ResourceNotFoundException;
import edu.sjsu.expressnest.feeds.model.UserFollowers;
import edu.sjsu.expressnest.feeds.repository.UserFollowersRepository;

@Service
public class UserFollowersService {

	@Autowired
	private UserFollowersRepository userFollowersRepository;

	public UserFollowers getFollowersForUser(Long userId) throws ResourceNotFoundException {
		return userFollowersRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("UserId does not exists"));
	}
}
