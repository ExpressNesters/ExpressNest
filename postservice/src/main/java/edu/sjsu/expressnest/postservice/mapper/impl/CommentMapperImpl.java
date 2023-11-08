package edu.sjsu.expressnest.postservice.mapper.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import edu.sjsu.expressnest.postservice.dto.CommentDTO;
import edu.sjsu.expressnest.postservice.mapper.CommentMapper;
import edu.sjsu.expressnest.postservice.model.Comment;

@Component
public class CommentMapperImpl implements CommentMapper{

	@Override
	public Comment toComment(CommentDTO commentDTO) {
		if (commentDTO == null) {
			return null;
		}
		return Comment.builder()
				.commentId(commentDTO.getCommentId())
				.commentText(commentDTO.getCommentText())
				.userId(commentDTO.getUserId())
				.build();
	}

	@Override
	public CommentDTO toCommentDTO(Comment comment) {
		if (comment == null) {
			return null;
		}
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

	@Override
	public List<CommentDTO> toCommentDTOs(List<Comment> comments) {
		if (comments == null) {
			return Collections.emptyList();
		}
		
		return comments.stream()
				.map(this::toCommentDTO)
				.collect(Collectors.toList());
	}

}
