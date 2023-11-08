package edu.sjsu.expressnest.postservice.dto;

import java.util.Date;
import java.util.List;

import edu.sjsu.expressnest.postservice.util.PrivacyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostDTO {
	
	@NotNull(message = "{error.post.userId.null}")
	@PositiveOrZero(message = "{error.post.userId.nan}")
	private long userId;
	
	private long postId;
	
	@NotBlank(message = "{error.post.text.blank}")
    @Size(max = 250, message = "{error.post.text.size}")
	private String postText;
	
	@NotNull(message = "{error.post.privacy.null}")
	private PrivacyType privacy;
	
	private List<AttachmentDTO> attachmentDTOs;
	private List<CommentDTO> commentDTOs;
	private List<ReactionDTO> reactionDTOs;
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;
}
