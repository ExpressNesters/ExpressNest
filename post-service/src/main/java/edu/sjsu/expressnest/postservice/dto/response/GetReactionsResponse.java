package edu.sjsu.expressnest.postservice.dto.response;

import java.util.List;

import edu.sjsu.expressnest.postservice.dto.ReactionDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetReactionsResponse {
	
	private List<ReactionDTO> reactionDTOs;
	private int currentPage;
    private long totalItems;
    private int totalPages;
    private int pageSize;

}
