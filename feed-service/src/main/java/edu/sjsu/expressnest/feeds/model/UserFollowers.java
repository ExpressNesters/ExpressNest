package edu.sjsu.expressnest.feeds.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "UsersFollowers")
public class UserFollowers {
	
	private long userId;
	private List<Long> followerIds;

}
