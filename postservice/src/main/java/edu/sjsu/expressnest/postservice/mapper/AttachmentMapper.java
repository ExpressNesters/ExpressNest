package edu.sjsu.expressnest.postservice.mapper;

import java.util.List;

import edu.sjsu.expressnest.postservice.dto.AttachmentDTO;
import edu.sjsu.expressnest.postservice.model.Attachment;

public interface AttachmentMapper {
	
	Attachment toAttachment(AttachmentDTO attachmentDTO);
	
	AttachmentDTO toAttachmentDTO(Attachment attachment);
	
	List<AttachmentDTO> toAttachmentDTOs(List<Attachment> attachments);

}
