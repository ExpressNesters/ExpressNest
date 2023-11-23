package edu.sjsu.expressnest.feeds.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "UsersFeeds")
public class UserFeeds {
	
	@Id
	private long userId;
	private List<Long> postIds;											

}
