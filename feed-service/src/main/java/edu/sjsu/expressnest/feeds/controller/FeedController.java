package edu.sjsu.expressnest.feeds.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.expressnest.feeds.exception.ResourceNotFoundException;
import edu.sjsu.expressnest.feeds.model.UserFeeds;
import edu.sjsu.expressnest.feeds.service.UserFeedService;

@RestController
@RequestMapping("/feeds")
public class FeedController {

    @Autowired
    private UserFeedService userFeedService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserFeeds> getFeedsForUser(@PathVariable Long userId) throws ResourceNotFoundException {
        UserFeeds userFeeds = userFeedService.getFeedsForUser(userId);
        return ResponseEntity.ok(userFeeds);
    }
}
