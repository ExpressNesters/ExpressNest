package edu.sjsu.expressnest.postservice.dto.response;

import java.util.List;

import edu.sjsu.expressnest.postservice.dto.PostDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetPostsResponse {
	
	List<PostDTO> postDTOs;

}
