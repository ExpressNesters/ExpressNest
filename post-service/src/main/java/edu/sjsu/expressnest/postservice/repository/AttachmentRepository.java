package edu.sjsu.expressnest.postservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.sjsu.expressnest.postservice.model.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
	
	List<Attachment> findByPost_PostId(long postId);

}
