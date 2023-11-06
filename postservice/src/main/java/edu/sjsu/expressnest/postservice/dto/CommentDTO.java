package edu.sjsu.expressnest.postservice.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CommentDTO {
	
	private String commentText;
	private long userId;
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;

}
