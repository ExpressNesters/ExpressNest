package edu.sjsu.expressnest.feeds.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.sjsu.expressnest.feeds.model.UserFeeds;

public interface UserFeedsRepository extends MongoRepository<UserFeeds, Long> {

}
