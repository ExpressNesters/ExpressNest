package edu.sjsu.expressnest.postservice.dto.response;

import java.util.List;

import edu.sjsu.expressnest.postservice.dto.CommentDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetCommentsResponse {
	
	private List<CommentDTO> commentDTOs;
	private int currentPage;
    private long totalItems;
    private int totalPages;
    private int pageSize;

}
