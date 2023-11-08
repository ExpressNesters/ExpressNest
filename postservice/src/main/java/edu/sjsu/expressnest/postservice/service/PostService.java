package edu.sjsu.expressnest.postservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.expressnest.postservice.dto.PostDTO;
import edu.sjsu.expressnest.postservice.exception.ResourceNotFoundException;
import edu.sjsu.expressnest.postservice.mapper.PostMapper;
import edu.sjsu.expressnest.postservice.model.Post;
import edu.sjsu.expressnest.postservice.repository.PostRepository;
import edu.sjsu.expressnest.postservice.util.PostServiceConstants;

@Service
public class PostService {
	
	@Autowired
	private PostMapper postMapper;
	
	@Autowired
	private AttachmentService attachmentService;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private ReactionService reactionService;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private ValidationMessageService validationMessageService;
	
	public PostDTO createPost(PostDTO postDTO) {
		// Convert DTO to entity
	    Post postEntity = postMapper.toPost(postDTO);
	    
	    // Map the attachments
        if (postDTO.getAttachmentDTOs() != null) {
        	attachmentService.mapAttachments(postDTO, postEntity);
        }
        
        // Map the comments
        if (postDTO.getCommentDTOs() != null) {
        	commentService.mapComments(postDTO, postEntity);
        }
        
        // Map the reactions
        if (postDTO.getReactionDTOs() != null) {
        	reactionService.mapReactions(postDTO, postEntity);
        }
	    
	    // Save the entity to the database
	    Post savedPost = postRepository.save(postEntity);
	    
	    // Convert the saved entity back to DTO
	    return postMapper.toPostDTO(savedPost);
	}
	
	public PostDTO getPostByPostId(long postId) throws ResourceNotFoundException {
		Post retrievedPost = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException(
						validationMessageService.getValidationMessage(PostServiceConstants.POST_NOT_FOUND_KEY, postId), 
						String.valueOf(postId)));
		
		return postMapper.toPostDTO(retrievedPost);
	}

}
                                                         