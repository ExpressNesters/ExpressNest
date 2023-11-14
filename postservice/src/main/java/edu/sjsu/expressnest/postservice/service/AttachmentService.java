package edu.sjsu.expressnest.postservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.expressnest.postservice.dto.PostDTO;
import edu.sjsu.expressnest.postservice.mapper.impl.AttachmentMapper;
import edu.sjsu.expressnest.postservice.model.Attachment;
import edu.sjsu.expressnest.postservice.model.Post;

@Service
public class AttachmentService {
	
	@Autowired
	private AttachmentMapper attachmentMapper;
	
	public void mapAttachments(PostDTO postDTO, Post postEntity) {
		List<Attachment> attachments = postDTO.getAttachmentDTOs().stream()
		        .map(attachmentMapper::toAttachment)
		        .peek(attachment -> attachment.setPost(postEntity)) // Setting the post in the service layer
		        .collect(Collectors.toList());
		postEntity.setAttachments(attachments);
	}

}
