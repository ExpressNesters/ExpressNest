package edu.sjsu.expressnest.postservice.dto;

import java.util.Date;

import edu.sjsu.expressnest.postservice.util.AttachmentType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttachmentDTO {
	
	private long attachmentId;
	
	@NotNull(message = "{error.attachment.attachmentType.null}")
	private AttachmentType attachmentType;
	
	private String attachmentRef;
	
	@NotNull(message = "{error.attachment.userId.null}")
	private long userId;
	
	@NotNull(message = "{error.attachment.postId.null}")
	private long postId;
	
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;

}
