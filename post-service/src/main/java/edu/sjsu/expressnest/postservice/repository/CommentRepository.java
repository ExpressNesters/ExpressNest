package edu.sjsu.expressnest.postservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import edu.sjsu.expressnest.postservice.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	Page<Comment> findByPost_PostId(long postId, Pageable pageable);
	
	Page<Comment> findByUserId(long userId, Pageable pageable);

}
