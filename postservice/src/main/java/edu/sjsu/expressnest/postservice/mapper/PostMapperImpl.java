package edu.sjsu.expressnest.postservice.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import edu.sjsu.expressnest.postservice.dto.PostDTO;
import edu.sjsu.expressnest.postservice.model.Post;

@Component
public class PostMapperImpl implements PostMapper {
	
	@Override
	public Post toPost(PostDTO postDTO) {
		return null;
	}
    
	@Override
    public PostDTO toPostDTO(Post post) {
		return null;
	}

	@Override
	public List<PostDTO> toPostDTOs(List<Post> posts) {
		return null;
	}

}
