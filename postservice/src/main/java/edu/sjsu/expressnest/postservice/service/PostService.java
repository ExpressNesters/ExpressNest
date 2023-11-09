package edu.sjsu.expressnest.postservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.expressnest.postservice.dto.PostDTO;
import edu.sjsu.expressnest.postservice.exception.ResourceNotFoundException;
import edu.sjsu.expressnest.postservice.mapper.PostMapper;
import edu.sjsu.expressnest.postservice.model.Post;
import edu.sjsu.expressnest.postservice.model.User;
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
	private UserService userService;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private MessageService messageService;

	public PostDTO createPost(PostDTO postDTO) {

		Post postEntity = postMapper.toPost(postDTO);
		if (postDTO.getAttachmentDTOs() != null) {
			attachmentService.mapAttachments(postDTO, postEntity);
		}
		if (postDTO.getCommentDTOs() != null) {
			commentService.mapComments(postDTO, postEntity);
		}
		if (postDTO.getReactionDTOs() != null) {
			reactionService.mapReactions(postDTO, postEntity);
		}
		Post savedPost = postRepository.save(postEntity);
		return postMapper.toPostDTO(savedPost);
	}

	public PostDTO getPostByPostId(long postId) throws ResourceNotFoundException {
		Post retrievedPost = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException(
						messageService.getMessage(PostServiceConstants.POST_NOT_FOUND_ERROR_KEY, postId),
						String.valueOf(postId)));

		return postMapper.toPostDTO(retrievedPost);
	}

	public List<PostDTO> getPostsByUserId(long userId) throws ResourceNotFoundException {
		User user = userService.getUserByUserId(userId);
		List<Post> posts = postRepository.findByUserId(user.getUserId());
		return postMapper.toPostDTOs(posts);
	}

	public List<PostDTO> getPostsByPostIds(List<Long> postIds) {
		List<Post> posts = postRepository.findAllById(postIds);
		return postMapper.toPostDTOs(posts);
	}

	public String deletePost(long postId) throws ResourceNotFoundException {
		return postRepository.findById(postId).map(post -> {
			postRepository.delete(post);
			return messageService.getMessage(PostServiceConstants.POST_DELETED_INFO_KEY, Long.toString(postId));
		}).orElseThrow(() -> new ResourceNotFoundException(
				messageService.getMessage(PostServiceConstants.POST_NOT_FOUND_ERROR_KEY, postId),
				String.valueOf(postId)));
	}
}
