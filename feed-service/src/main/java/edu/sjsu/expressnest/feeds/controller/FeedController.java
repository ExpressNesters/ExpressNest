package edu.sjsu.expressnest.feeds.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.expressnest.feeds.exception.ResourceNotFoundException;
import edu.sjsu.expressnest.feeds.model.UserFeeds;
import edu.sjsu.expressnest.feeds.model.UserFollowers;
import edu.sjsu.expressnest.feeds.service.UserFeedService;
import edu.sjsu.expressnest.feeds.service.UserFollowersService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/feeds")
public class FeedController {

    @Autowired
    private UserFeedService userFeedService;

    @Autowired
    private UserFollowersService userFollowersService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserFeeds> getFeedsForUser(@PathVariable Long userId) throws ResourceNotFoundException {
        UserFeeds userFeeds = userFeedService.getFeedsForUser(userId);
        return ResponseEntity.ok(userFeeds);
    }

    @GetMapping("/user/{userId}/followers")
    public ResponseEntity<UserFollowers> getFollowersForUser(@PathVariable Long userId) throws ResourceNotFoundException {
        UserFollowers userFollowers = userFollowersService.getFollowersForUser(userId);
        return ResponseEntity.ok(userFollowers);
    }
}
