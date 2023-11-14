package edu.sjsu.expressnest.postservice.mapper.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.sjsu.expressnest.postservice.dto.CommentDTO;
import edu.sjsu.expressnest.postservice.dto.request.CreateCommentRequest;
import edu.sjsu.expressnest.postservice.dto.request.UpdateCommentRequest;
import edu.sjsu.expressnest.postservice.dto.response.CreateCommentResponse;
import edu.sjsu.expressnest.postservice.dto.response.DeleteCommentResponse;
import edu.sjsu.expressnest.postservice.dto.response.GetCommentsResponse;
import edu.sjsu.expressnest.postservice.dto.response.UpdateCommentResponse;
import edu.sjsu.expressnest.postservice.model.Comment;
import edu.sjsu.expressnest.postservice.service.MessageService;
import edu.sjsu.expressnest.postservice.util.PostServiceConstants;

@Component
public class CommentMapper {
	
	@Autowired
	private MessageService messageService;
	
	public Comment toComment(CreateCommentRequest createCommentRequest) {
		//set the Post in the service layer
		return Comment.builder()
				.commentText(createCommentRequest.getCommentText())
				.userId(createCommentRequest.getUserId())
				.build();
	}
	
	public CreateCommentResponse toCreateCommentResponse(Comment comment) {
		return CreateCommentResponse.builder()
				.createdCommentDTO(toCommentDTO(comment))
				.build();
	}
	
	public void mapUpdateCommentRequestToComment(UpdateCommentRequest updateCommentRequest, Comment comment) {
		if (updateCommentRequest !=null && comment != null) {
			comment.setCommentText(updateCommentRequest.getCommentText());
		}
	}
	
	public UpdateCommentResponse toUpdateCommentResponse(Comment comment) {
		return UpdateCommentResponse.builder()
				.updatedCommentDTO(toCommentDTO(comment))
				.build();
	}
	
	public GetCommentsResponse toGetCommentsResponse(List<Comment> comments) {
		return GetCommentsResponse.builder()
				.commentDTOs(toCommentDTOs(comments))
				.build();
	}
	
	public DeleteCommentResponse toDeleteCommentResponse(long commentId) {
		return DeleteCommentResponse.builder()
				.commentId(commentId)
				.message(messageService.getMessage(PostServiceConstants.COMMENT_DELETED_INFO_KEY, commentId))
				.build();
	}
	
	private List<CommentDTO> toCommentDTOs(List<Comment> comments) {
		return comments.stream()
				.map(this::toCommentDTO)
				.collect(Collectors.toList());
	}
	
	private CommentDTO toCommentDTO(Comment comment) {
		return CommentDTO.builder()
				.commentId(comment.getCommentId())
				.commentText(comment.getCommentText())
				.userId(comment.getUserId())
				.postId(comment.getPost().getPostId())
				.createdAt(comment.getCreatedAt())
				.updatedAt(comment.getUpdatedAt())
				.deletedAt(comment.getDeletedAt())
				.build();
	}

}
