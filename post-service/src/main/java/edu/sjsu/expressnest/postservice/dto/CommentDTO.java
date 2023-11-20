package edu.sjsu.expressnest.postservice.dto;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDTO {
	
	private long commentId;
	private String commentText;
	private long userId;
	private long postId;
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;

}
