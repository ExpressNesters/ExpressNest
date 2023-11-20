package edu.sjsu.expressnest.postservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import edu.sjsu.expressnest.postservice.model.Reaction;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
	
	Page<Reaction> findByPost_PostId(long postId, Pageable pageable);
	
	Page<Reaction> findByUserId(long userId, Pageable pageable);

}