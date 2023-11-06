package edu.sjsu.expressnest.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.sjsu.expressnest.postservice.model.Post;

public interface PostRepository extends JpaRepository<Post, Long>{

}
