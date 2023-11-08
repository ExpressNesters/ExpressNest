package edu.sjsu.expressnest.postservice.dto;

import java.util.Date;

import edu.sjsu.expressnest.postservice.util.AttachmentType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttachmentDTO {
	
	private long attachmentId;
	private AttachmentType attachmentType;
	private String attachmentRef;
	private long userId;
	private long postId;
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;

}
