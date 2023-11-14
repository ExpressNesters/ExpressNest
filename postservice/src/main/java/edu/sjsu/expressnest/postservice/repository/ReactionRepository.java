package edu.sjsu.expressnest.postservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.sjsu.expressnest.postservice.model.Reaction;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
	
	List<Reaction> findByPost_PostId(long postId);
	
	List<Reaction> findByUserId(long userId);

}