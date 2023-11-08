package edu.sjsu.expressnest.postservice.mapper.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import edu.sjsu.expressnest.postservice.dto.AttachmentDTO;
import edu.sjsu.expressnest.postservice.mapper.AttachmentMapper;
import edu.sjsu.expressnest.postservice.model.Attachment;

@Component
public class AttachmentMapperImpl implements AttachmentMapper{

	@Override
	public Attachment toAttachment(AttachmentDTO attachmentDTO) {		
		if (attachmentDTO == null) {
			return null;
		}		
		return Attachment.builder()
				.attachmentId(attachmentDTO.getAttachmentId())
				.attachmentType(attachmentDTO.getAttachmentType())
				.attachmentRef(attachmentDTO.getAttachmentRef())
				.userId(attachmentDTO.getUserId())
				.build();
	}

	@Override
	public AttachmentDTO toAttachmentDTO(Attachment attachment) {	
		if (attachment == null) {
			return null;
		}
		return AttachmentDTO.builder()
				.attachmentId(attachment.getAttachmentId())
				.attachmentType(attachment.getAttachmentType())
				.attachmentRef(attachment.getAttachmentRef())
				.userId(attachment.getUserId())
				.postId(attachment.getPost().getPostId())
				.createdAt(attachment.getCreatedAt())
				.updatedAt(attachment.getUpdatedAt())
				.deletedAt(attachment.getDeletedAt())
				.build();
	}

	@Override
	public List<AttachmentDTO> toAttachmentDTOs(List<Attachment> attachments) {
		if (attachments ==  null) {
			return Collections.emptyList();
		}
		return attachments.stream()
				.map(this::toAttachmentDTO)
				.collect(Collectors.toList());
	}

}
