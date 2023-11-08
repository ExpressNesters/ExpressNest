package edu.sjsu.expressnest.postservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.expressnest.postservice.dto.PostDTO;
import edu.sjsu.expressnest.postservice.mapper.CommentMapper;
import edu.sjsu.expressnest.postservice.model.Comment;
import edu.sjsu.expressnest.postservice.model.Post;

@Service
public class CommentService {
	
	@Autowired
	private CommentMapper commentMapper;
	
	public void mapComments(PostDTO postDTO, Post postEntity) {
		List<Comment> comments = postDTO.getCommentDTOs().stream()
				.map(commentMapper::toComment)
				.peek(comment -> comment.setPost(postEntity))
				.collect(Collectors.toList());
		postEntity.setComments(comments);
	}

}
