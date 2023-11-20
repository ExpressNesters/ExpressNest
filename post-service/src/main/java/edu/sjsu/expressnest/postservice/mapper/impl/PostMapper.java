package edu.sjsu.expressnest.postservice.mapper.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import edu.sjsu.expressnest.postservice.dto.PostDTO;
import edu.sjsu.expressnest.postservice.dto.request.CreatePostRequest;
import edu.sjsu.expressnest.postservice.dto.request.UpdatePostRequest;
import edu.sjsu.expressnest.postservice.dto.response.CreatePostResponse;
import edu.sjsu.expressnest.postservice.dto.response.DeletePostResponse;
import edu.sjsu.expressnest.postservice.dto.response.GetPostResponse;
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
				.build();
	}
	
	public CreatePostResponse toCreatePostResponse(Post post) {
		if (post == null) {
			return null;
		}
		
		PostDTO createdPostDTO = toPostDTO(post);
		return CreatePostResponse.builder()
				.createdPostDTO(createdPostDTO)
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
	
	public GetPostsResponse toGetPostsResponse(Page<Post> postsPage) {
		return GetPostsResponse.builder()
				.postDTOs(toPostDTOs(postsPage))
				.currentPage(postsPage.getNumber())
				.pageSize(postsPage.getSize())
				.totalItems(postsPage.getTotalElements())
                .totalPages(postsPage.getTotalPages())
				.build();
	}
	
	public GetPostResponse toGetPostResponse(Post post) {
		return GetPostResponse.builder()
				.postDTO(toPostDTO(post))
				.build();
	}

	
	public DeletePostResponse toDeletePostResponse(long postId) {
		return DeletePostResponse.builder()
				.postId(postId)
				.message(messageService.getMessage(PostServiceConstants.POST_DELETED_INFO_KEY, postId))
				.build();
	}
	
	private List<PostDTO> toPostDTOs(Page<Post> postsPage) {
		return postsPage.getContent().stream()
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
