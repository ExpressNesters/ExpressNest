package edu.sjsu.expressnest.feeds.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.sjsu.expressnest.feeds.model.UserFollowers;

public interface UserFollowersRepository extends MongoRepository<UserFollowers, Long> {

}
