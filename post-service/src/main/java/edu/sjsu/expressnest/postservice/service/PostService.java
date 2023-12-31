package edu.sjsu.expressnest.postservice.service;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.sjsu.expressnest.postservice.dto.request.CreatePostRequest;
import edu.sjsu.expressnest.postservice.dto.request.UpdatePostRequest;
import edu.sjsu.expressnest.postservice.dto.response.CreatePostResponse;
import edu.sjsu.expressnest.postservice.dto.response.DeletePostResponse;
import edu.sjsu.expressnest.postservice.dto.response.GetPostResponse;
import edu.sjsu.expressnest.postservice.dto.response.GetPostsResponse;
import edu.sjsu.expressnest.postservice.dto.response.UpdatePostResponse;
import edu.sjsu.expressnest.postservice.exception.ResourceNotFoundException;
import edu.sjsu.expressnest.postservice.mapper.impl.PostMapper;
import edu.sjsu.expressnest.postservice.messaging.PostEvent;
import edu.sjsu.expressnest.postservice.messaging.PostEventProducer;
import edu.sjsu.expressnest.postservice.model.Attachment;
import edu.sjsu.expressnest.postservice.model.Post;
import edu.sjsu.expressnest.postservice.repository.PostRepository;
import edu.sjsu.expressnest.postservice.util.PostEventType;
import edu.sjsu.expressnest.postservice.util.PostServiceConstants;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PostService {

	@Autowired
	private PostMapper postMapper;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private MessageService messageService;
	
	@Autowired
	private AttachmentService attachmentService;
	
	@Autowired
	private PostEventProducer postEventProducer;
	
	@Autowired
	private RedisCacheManager redisCacheManager;

	@Cacheable(value="Post", key="#postId")
	public GetPostResponse getPostByPostId(long postId) throws ResourceNotFoundException {
		log.info("Post Service : getPostByPostId with PostId={}", postId);
		return postRepository.findById(postId)
				.map(postMapper::toGetPostResponse)
				.orElseThrow(() -> new ResourceNotFoundException(
						messageService.getMessage(PostServiceConstants.POST_NOT_FOUND_ERROR_KEY, postId)));
	}

	public GetPostsResponse getPostsByUserId(long userId, Pageable pageable) {
		log.info("Post Service : getPostsByUserId with UserId={}", userId);
		Page<Post> posts = postRepository.findByUserId(userId, pageable);
		return postMapper.toGetPostsResponse(posts);
	}

	public GetPostsResponse getPostsByPostIds(List<Long> postIds, Pageable pageable) {
		log.info("Post Service : getPostsByPostIds with postIds={}", postIds);
		Page<Post> posts = postRepository.findByPostIdIn(postIds, pageable);
		return postMapper.toGetPostsResponse(posts);
	}
	
	@Transactional
	@CachePut(value="Post", key="#result.createdPostDTO.postId")
	public CreatePostResponse createPost(CreatePostRequest createPostRequest) {
		log.info("Post Service : createPost");
		Post post = postMapper.toPost(createPostRequest);
		post.setTotalNoOfComments(0);
		post.setTotalNoOfReactions(0);
		Post createdPost = postRepository.save(post);
		PostEvent postEvent = PostEvent.builder()
				.postId(createdPost.getPostId())
				.actionedBy(createdPost.getUserId())
				.postEventType(PostEventType.CREATE)
				.build();
		postEventProducer.sendEvent(postEvent);
		return postMapper.toCreatePostResponse(createdPost);
	}
	
	@Transactional
	@CachePut(value="Post", key="#result.createdPostDTO.postId")
	public CreatePostResponse createPostWithAttachment(CreatePostRequest createPostRequest, MultipartFile file) throws IOException {
		log.info("Post Service : createPostWithAttachment");
		Post post = postMapper.toPost(createPostRequest);
		post.setTotalNoOfComments(0);
		post.setTotalNoOfReactions(0);
		if (file != null) {
			Attachment createdAttachment = attachmentService.createAttachment(file);
			createdAttachment.setPost(post);
			post.setAttachments(Collections.singletonList(createdAttachment));
		}
		Post createdPost = postRepository.save(post);
		PostEvent postEvent = PostEvent.builder()
				.postId(createdPost.getPostId())
				.actionedBy(createdPost.getUserId())
				.postEventType(PostEventType.CREATE)
				.build();
		postEventProducer.sendEvent(postEvent);
		return postMapper.toCreatePostResponse(createdPost);
	}

	@Transactional
	@CacheEvict(value="Post", key="#postId")
	public DeletePostResponse deletePost(long postId) throws ResourceNotFoundException {
		// soft delete - need to check about setting comments and reactions as deleted.
		log.info("Post Service : deletePost with postId={}", postId);
		return postRepository.findById(postId)
				.map(post -> {
					post.setDeletedAt(new Date());
					post.getAttachments().forEach(attachment -> {
	                    attachmentService.deleteAttachmentByAttachmentId(attachment.getAttachmentId());
	                });
					post.getComments().forEach(comment -> {
						comment.setDeletedAt(new Date());
					});
					post.getReactions().forEach(reaction -> {
						reaction.setDeletedAt(new Date());
					});
					Post deletedPost = postRepository.save(post);
					PostEvent postEvent = PostEvent.builder()
							.postId(deletedPost.getPostId())
							.actionedBy(deletedPost.getUserId())
							.postEventType(PostEventType.DELETE)
							.build();
					postEventProducer.sendEvent(postEvent);
					return postMapper.toDeletePostResponse(postId);
					})
				.orElseThrow(() -> new ResourceNotFoundException(
						messageService.getMessage(PostServiceConstants.POST_NOT_FOUND_ERROR_KEY, postId)));
	}
	
	@Transactional
	@CachePut(value="Post", key="#postId")
	public UpdatePostResponse updatePost(long postId, UpdatePostRequest updatePostRequest) throws ResourceNotFoundException {
		log.info("Post Service : updatePost with postId={}", postId);
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException(
						messageService.getMessage(PostServiceConstants.POST_NOT_FOUND_ERROR_KEY, postId)));
		
		postMapper.mapUpdatePostRequestToPost(updatePostRequest, post);
		Post updatedPost = postRepository.save(post);
		PostEvent postEvent = PostEvent.builder()
				.postId(updatedPost.getPostId())
				.actionedBy(updatedPost.getUserId())
				.postEventType(PostEventType.UPDATE).build();
		postEventProducer.sendEvent(postEvent);
		return postMapper.toUpdatePostResponse(updatedPost);
	}
}
