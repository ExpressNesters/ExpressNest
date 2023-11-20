package edu.sjsu.expressnest.postservice.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import edu.sjsu.expressnest.postservice.model.Post;

public interface PostRepository extends JpaRepository<Post, Long>{
	
	public Page<Post> findByUserId(long userId, Pageable pageable);
	public Page<Post> findByPostIdIn(List<Long> postIds, Pageable pageable);

}
