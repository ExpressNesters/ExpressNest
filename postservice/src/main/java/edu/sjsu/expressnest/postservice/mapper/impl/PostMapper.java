package edu.sjsu.expressnest.postservice.mapper.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.sjsu.expressnest.postservice.dto.PostDTO;
import edu.sjsu.expressnest.postservice.dto.request.CreatePostRequest;
import edu.sjsu.expressnest.postservice.dto.request.UpdatePostRequest;
import edu.sjsu.expressnest.postservice.dto.response.CreatePostResponse;
import edu.sjsu.expressnest.postservice.dto.response.DeletePostResponse;
import edu.sjsu.expressnest.postservice.dto.response.GetPostsResponse;
import edu.sjsu.expressnest.postservice.dto.response.UpdatePostResponse;
import edu.sjsu.expressnest.postservice.model.Post;
import edu.sjsu.expressnest.postservice.service.MessageService;
import edu.sjsu.expressnest.postservice.util.PostServiceConstants;

@Component
public class PostMapper {
	
	@Autowired
	AttachmentMapper attachmentMapper;
	
	@Autowired
	MessageService messageService;
	
	public Post toPost(CreatePostRequest createPostRequest) {
		if (createPostRequest == null) {
			return null;
		}
		return Post.builder()
				.userId(createPostRequest.getUserId())
				.postText(createPostRequest.getPostText())
				.privacy(createPostRequest.getPrivacy())
				.attachments(null)
				.build();
	}
	
	public CreatePostResponse toCreatePostResponse(Post post) {
		if (post == null) {
			return null;
		}
		return CreatePostResponse.builder()
				.createdPostDTO(toPostDTO(post))
				.build();
	}
	
	public void mapUpdatePostRequestToPost(UpdatePostRequest updatePostRequest, Post post) {
		if(updatePostRequest != null && post !=null) {
			post.setPostText(updatePostRequest.getPostText());
			post.setPrivacy(updatePostRequest.getPrivacy());
			post.setAttachments(null);
		}
	}
	
	public UpdatePostResponse toUpdatePostResponse(Post post) {
		if (post == null) {
			return null;
		}
		return UpdatePostResponse.builder()
				.updatedPostDTO(toPostDTO(post))
				.build();
	}
	
	public GetPostsResponse toGetPostResponse(List<Post> posts) {
		return GetPostsResponse.builder()
				.postDTOs(toPostPostDTOs(posts))
				.build();
	}
	
	public DeletePostResponse toDeletePostResponse(long postId) {
		return DeletePostResponse.builder()
				.postId(postId)
				.message(messageService.getMessage(PostServiceConstants.POST_DELETED_INFO_KEY, postId))
				.build();
	}
	
	private List<PostDTO> toPostPostDTOs(List<Post> posts) {
		return posts.stream()
				.map(this::toPostDTO)
				.collect(Collectors.toList());
	}

	private PostDTO toPostDTO(Post post) {
		if (post == null) {
			return null;
		}
		
		return PostDTO.builder()
				.userId(post.getUserId())
				.postId(post.getPostId())
				.postText(post.getPostText())
				.privacy(post.getPrivacy())
				.attachmentDTOs(null)
				.totalNoOfComments(post.getTotalNoOfComments())
				.totalNoOfReactions(post.getTotalNoOfReactions())
				.createdAt(post.getCreatedAt())
				.updatedAt(post.getUpdatedAt())
				.deletedAt(post.getDeletedAt())
				.build();
	}

}
