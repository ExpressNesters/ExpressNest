package edu.sjsu.expressnest.postservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.expressnest.postservice.dto.PostDTO;
import edu.sjsu.expressnest.postservice.mapper.ReactionMapper;
import edu.sjsu.expressnest.postservice.model.Post;
import edu.sjsu.expressnest.postservice.model.Reaction;

@Service
public class ReactionService {
	
	@Autowired
	private ReactionMapper reactionMapper;
	
	public void mapReactions(PostDTO postDTO, Post postEntity) {
		List<Reaction> reactions = postDTO.getReactionDTOs().stream()
				.map(reactionMapper::toReaction)
				.peek(reaction -> reaction.setPost(postEntity))
				.collect(Collectors.toList());
		postEntity.setReactions(reactions);
	}

}
