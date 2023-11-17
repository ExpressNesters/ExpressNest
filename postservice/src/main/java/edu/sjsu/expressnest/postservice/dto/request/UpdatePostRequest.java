package edu.sjsu.expressnest.postservice.dto.request;

import java.io.Serializable;

import edu.sjsu.expressnest.postservice.util.PrivacyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePostRequest implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4277466902522976435L;

	@NotNull(message = "{error.post.userId.null}")
	@PositiveOrZero(message = "{error.post.userId.nan}")
	private long userId;
	
	private long postId;
	
	@NotBlank(message = "{error.post.text.blank}")
    @Size(max = 250, message = "{error.post.text.size}")
	private String postText;
	
	@NotNull(message = "{error.post.privacy.null}")
	private PrivacyType privacy;

}
