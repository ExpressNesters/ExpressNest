package edu.sjsu.expressnest.postservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.sjsu.expressnest.postservice.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	List<Comment> findByPost_PostId(long postId);
	
	List<Comment> findByUserId(long userId);

}
