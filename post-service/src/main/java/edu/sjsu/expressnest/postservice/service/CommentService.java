package edu.sjsu.expressnest.postservice.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.sjsu.expressnest.postservice.dto.request.CreateCommentRequest;
import edu.sjsu.expressnest.postservice.dto.request.UpdateCommentRequest;
import edu.sjsu.expressnest.postservice.dto.response.CreateCommentResponse;
import edu.sjsu.expressnest.postservice.dto.response.DeleteCommentResponse;
import edu.sjsu.expressnest.postservice.dto.response.GetCommentResponse;
import edu.sjsu.expressnest.postservice.dto.response.GetCommentsResponse;
import edu.sjsu.expressnest.postservice.dto.response.UpdateCommentResponse;
import edu.sjsu.expressnest.postservice.exception.ResourceNotFoundException;
import edu.sjsu.expressnest.postservice.mapper.impl.CommentMapper;
import edu.sjsu.expressnest.postservice.model.Comment;
import edu.sjsu.expressnest.postservice.model.Post;
import edu.sjsu.expressnest.postservice.repository.CommentRepository;
import edu.sjsu.expressnest.postservice.repository.PostRepository;
import edu.sjsu.expressnest.postservice.util.PostServiceConstants;
import jakarta.transaction.Transactional;

@Service
public class CommentService {

	@Autowired
	private CommentMapper commentMapper;

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private MessageService messageService;

	@Transactional
	@CachePut(value="Comment", key="#result.createdCommentDTO.commentId")
	public CreateCommentResponse createComment(CreateCommentRequest createCommentRequest) throws ResourceNotFoundException {
		long postId = createCommentRequest.getPostId();
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException(
						messageService.getMessage(PostServiceConstants.POST_NOT_FOUND_ERROR_KEY, postId)));
		int currentTotalComments = post.getTotalNoOfComments();
		post.setTotalNoOfComments(currentTotalComments + 1);
		Comment comment = commentMapper.toComment(createCommentRequest);
		comment.setPost(post);
		Comment createdComment = commentRepository.save(comment);
		postRepository.save(post);
		return commentMapper.toCreateCommentResponse(createdComment);
	}
	
	@Transactional
	@CachePut(value="Comment", key="#commentId")
	public UpdateCommentResponse updateComment(long commentId, UpdateCommentRequest updateCommentRequest) throws ResourceNotFoundException {
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException(
						messageService.getMessage(PostServiceConstants.COMMENT_NOT_FOUND_ERROR_KEY, commentId)));
		commentMapper.mapUpdateCommentRequestToComment(updateCommentRequest, comment);
		Comment updatedComment = commentRepository.save(comment);
		return commentMapper.toUpdateCommentResponse(updatedComment);
	}
	
	@Transactional
	@CacheEvict(value="Comment", key="#commentId")
	public DeleteCommentResponse deleteComment(long commentId) throws ResourceNotFoundException {
		// soft delete
		Comment comment = commentRepository.findById(commentId)
		.orElseThrow(() -> new ResourceNotFoundException(
				messageService.getMessage(PostServiceConstants.COMMENT_NOT_FOUND_ERROR_KEY, commentId)));
		
		Post post = comment.getPost();
		int currentTotalComments = post.getTotalNoOfComments();
		comment.setDeletedAt(new Date());
		commentRepository.save(comment);
		post.setTotalNoOfComments(Math.max(0, currentTotalComments - 1));
		postRepository.save(post);
		
		return commentMapper.toDeleteCommentResponse(commentId);
	}
	
	@Cacheable(value="Comment", key="#commentId")
	public GetCommentResponse getCommentByCommentId(long commentId) throws ResourceNotFoundException {
		return commentRepository.findById(commentId)
				.map(commentMapper::toGetCommentResponse)
				.orElseThrow(() -> new ResourceNotFoundException(
						messageService.getMessage(PostServiceConstants.COMMENT_NOT_FOUND_ERROR_KEY, commentId)));
	}

	public GetCommentsResponse getCommentsByPostId(long postId, Pageable pageable) {
		Page<Comment> commentsPage = commentRepository.findByPost_PostId(postId, pageable);
		return commentMapper.toGetCommentsResponse(commentsPage);
	}

	public GetCommentsResponse getCommentsByUserId(long userId, Pageable pageable) {
		Page<Comment> commentsPage = commentRepository.findByUserId(userId, pageable);
		return commentMapper.toGetCommentsResponse(commentsPage);
	}

}
