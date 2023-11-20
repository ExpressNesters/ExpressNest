package edu.sjsu.expressnest.postservice.dto.response;

import edu.sjsu.expressnest.postservice.dto.CommentDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetCommentResponse {

	private CommentDTO commentDTO;
}
