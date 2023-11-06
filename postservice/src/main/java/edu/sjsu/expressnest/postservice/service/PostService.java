package edu.sjsu.expressnest.postservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.expressnest.postservice.dto.PostDTO;
import edu.sjsu.expressnest.postservice.mapper.PostMapper;
import edu.sjsu.expressnest.postservice.model.Post;
import edu.sjsu.expressnest.postservice.repository.PostRepository;

@Service
public class PostService {
	
	@Autowired
	private PostMapper postMapper;
	
	@Autowired
	private PostRepository postRepository;
	
	public PostDTO createPost(PostDTO postDTO) {
		Post post = postMapper.toPost(postDTO);
		PostDTO postDTO2 = postMapper.toPostDTO(postRepository.save(post));		
		return postDTO2;
	}

}
                                                         