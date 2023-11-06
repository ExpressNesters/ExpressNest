package edu.sjsu.expressnest.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.sjsu.expressnest.postservice.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{

}
