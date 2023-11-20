package edu.sjsu.expressnest.postservice.dto;

import java.net.URL;
import java.util.Date;

import edu.sjsu.expressnest.postservice.util.AttachmentType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttachmentDTO {
	
	private long attachmentId;
	private AttachmentType attachmentType;	
	private URL url;
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;

}
