package edu.sjsu.expressnest.postservice.mapper.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.sjsu.expressnest.postservice.dto.PostDTO;
import edu.sjsu.expressnest.postservice.mapper.AttachmentMapper;
import edu.sjsu.expressnest.postservice.mapper.CommentMapper;
import edu.sjsu.expressnest.postservice.mapper.PostMapper;
import edu.sjsu.expressnest.postservice.mapper.ReactionMapper;
import edu.sjsu.expressnest.postservice.model.Post;

@Component
public class PostMapperImpl implements PostMapper {
	
	@Autowired
	AttachmentMapper attachmentMapper;
	
	@Autowired
	CommentMapper commentMapper;
	
	@Autowired
	ReactionMapper reactionMapper;

	@Override
	public Post toPost(PostDTO postDTO) {
		if (postDTO == null) {
			return null;
		}
		return Post.builder()
				.postId(postDTO.getPostId())
				.userId(postDTO.getUserId())
				.postText(postDTO.getPostText())
				.privacy(postDTO.getPrivacy())
				.build();
	}

	@Override
	public PostDTO toPostDTO(Post post) {
		if (post ==  null) {
			return null;
		}
		return PostDTO.builder()
				.postId(post.getPostId())
				.userId(post.getUserId())
				.postText(post.getPostText())
				.privacy(post.getPrivacy())
				.attachmentDTOs(attachmentMapper.toAttachmentDTOs(post.getAttachments()))
				.commentDTOs(commentMapper.toCommentDTOs(post.getComments()))
				.reactionDTOs(reactionMapper.toReactionDTOs(post.getReactions()))
				.createdAt(post.getCreatedAt())
				.updatedAt(post.getUpdatedAt())
				.deletedAt(post.getDeletedAt())
				.build();
	}

	@Override
	public List<PostDTO> toPostDTOs(List<Post> posts) {
		if (posts == null) {
			return Collections.emptyList();
		}
		return posts.stream()
				.map(this::toPostDTO)
				.collect(Collectors.toList());
	}

}
