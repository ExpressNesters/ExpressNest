package edu.sjsu.expressnest.postservice.dto.response;

import edu.sjsu.expressnest.postservice.dto.PostDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePostResponse {

	private PostDTO createdPostDTO;

}
