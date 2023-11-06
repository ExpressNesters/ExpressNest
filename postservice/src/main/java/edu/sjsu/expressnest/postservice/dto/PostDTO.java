package edu.sjsu.expressnest.postservice.dto;

import java.util.Date;
import java.util.List;

import edu.sjsu.expressnest.postservice.util.PrivacyType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostDTO {
	
	private long userId;
	private long postId;
	private String postText;
	private PrivacyType privacy;
	private List<AttachmentDTO> attachments;
	private List<CommentDTO> comments;
	private List<ReactionDTO> reactions;
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;
}
