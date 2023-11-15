package edu.sjsu.expressnest.postservice.dto.response;

import edu.sjsu.expressnest.postservice.dto.PostDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetPostResponse {
	
	private PostDTO postDTO;

}
