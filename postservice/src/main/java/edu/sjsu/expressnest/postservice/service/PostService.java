package edu.sjsu.expressnest.postservice.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.expressnest.postservice.dto.request.CreatePostRequest;
import edu.sjsu.expressnest.postservice.dto.request.UpdatePostRequest;
import edu.sjsu.expressnest.postservice.dto.response.CreatePostResponse;
import edu.sjsu.expressnest.postservice.dto.response.DeletePostResponse;
import edu.sjsu.expressnest.postservice.dto.response.GetPostsResponse;
import edu.sjsu.expressnest.postservice.dto.response.UpdatePostResponse;
import edu.sjsu.expressnest.postservice.exception.ResourceNotFoundException;
import edu.sjsu.expressnest.postservice.mapper.impl.PostMapper;
import edu.sjsu.expressnest.postservice.model.Post;
import edu.sjsu.expressnest.postservice.repository.PostRepository;
import edu.sjsu.expressnest.postservice.util.PostServiceConstants;

@Service
public class PostService {

	@Autowired
	private PostMapper postMapper;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private MessageService messageService;

	public GetPostsResponse getPostByPostId(long postId) throws ResourceNotFoundException {
		return postRepository.findById(postId)
				.map(Collections::singletonList)
				.map(postMapper::toGetPostResponse)
				.orElseThrow(() -> new ResourceNotFoundException(
						messageService.getMessage(PostServiceConstants.POST_NOT_FOUND_ERROR_KEY, postId)));
	}

	public GetPostsResponse getPostsByUserId(long userId) {
		List<Post> posts = postRepository.findByUserId(userId);
		return postMapper.toGetPostResponse(posts);
	}

	public GetPostsResponse getPostsByPostIds(List<Long> postIds) {
		List<Post> posts = postRepository.findAllById(postIds);
		return postMapper.toGetPostResponse(posts);
	}
	
	public CreatePostResponse createPost(CreatePostRequest createPostRequest) {
		Post post = postMapper.toPost(createPostRequest);
		post.setTotalNoOfComments(0);
		post.setTotalNoOfReactions(0);
		Post createdPost = postRepository.save(post);
		return postMapper.toCreatePostResponse(createdPost);
	}

	public DeletePostResponse deletePost(long postId) throws ResourceNotFoundException {
		return postRepository.findById(postId)
				.map(post -> {
					postRepository.delete(post);
					return postMapper.toDeletePostResponse(postId);
					})
				.orElseThrow(() -> new ResourceNotFoundException(
						messageService.getMessage(PostServiceConstants.POST_NOT_FOUND_ERROR_KEY, postId)));
	}
	
	public UpdatePostResponse updatePost(UpdatePostRequest updatePostRequest) throws ResourceNotFoundException {
		long postId = updatePostRequest.getPostId();
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException(
						messageService.getMessage(PostServiceConstants.POST_NOT_FOUND_ERROR_KEY, postId)));
		
		postMapper.mapUpdatePostRequestToPost(updatePostRequest, post);
		Post updatedPost = postRepository.save(post);
		return postMapper.toUpdatePostResponse(updatedPost);
	}
}
