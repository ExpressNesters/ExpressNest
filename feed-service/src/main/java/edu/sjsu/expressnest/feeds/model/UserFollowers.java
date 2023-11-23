package edu.sjsu.expressnest.feeds.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "UsersFollowers")
public class UserFollowers {
	
	@Id
	private long userId;
	private List<Long> followerIds;

}
