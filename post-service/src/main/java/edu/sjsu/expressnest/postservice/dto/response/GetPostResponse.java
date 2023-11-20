package edu.sjsu.expressnest.postservice.dto.response;

import java.io.Serializable;

import edu.sjsu.expressnest.postservice.dto.PostDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetPostResponse implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9111804793075787162L;
	private PostDTO postDTO;

}
