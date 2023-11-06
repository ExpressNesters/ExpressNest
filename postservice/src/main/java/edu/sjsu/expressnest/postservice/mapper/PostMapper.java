package edu.sjsu.expressnest.postservice.mapper;

import java.util.List;

import edu.sjsu.expressnest.postservice.dto.PostDTO;
import edu.sjsu.expressnest.postservice.model.Post;

public interface PostMapper {
	
	 Post toPost(PostDTO postDTO);
    
     PostDTO toPostDTO(Post post);
     
     List<PostDTO> toPostDTOs(List<Post> posts);

}
