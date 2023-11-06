package edu.sjsu.expressnest.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.sjsu.expressnest.postservice.model.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Long>{

}
