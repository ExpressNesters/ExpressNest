package edu.sjsu.expressnest.postservice.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class PostResponseDTO {
	
	private long postId;
	private long userId;
	private String postText;
	private String privacy;
	private List<AttachmentDTO> attachments;
	private List<CommentDTO> comments;
	private List<ReactionDTO> reactions;
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;

}
