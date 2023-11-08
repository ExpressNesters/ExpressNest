package edu.sjsu.expressnest.postservice.mapper;


import java.util.List;

import edu.sjsu.expressnest.postservice.dto.CommentDTO;
import edu.sjsu.expressnest.postservice.model.Comment;

public interface CommentMapper {
	
	Comment toComment(CommentDTO commentDTO);
	
	CommentDTO toCommentDTO(Comment comment);
	
	List<CommentDTO> toCommentDTOs(List<Comment> comments);

}
