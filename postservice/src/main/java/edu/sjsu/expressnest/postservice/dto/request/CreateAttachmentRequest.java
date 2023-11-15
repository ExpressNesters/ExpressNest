package edu.sjsu.expressnest.postservice.dto.request;

import edu.sjsu.expressnest.postservice.util.AttachmentType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateAttachmentRequest {
	
	private AttachmentType attachmentType;	
	private String attachmentRef;
}
