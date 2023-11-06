package edu.sjsu.expressnest.postservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name = "en_users")
public class User {
	
	@Id
	private long userId;
	private String username;
	private String password;
	private String email;
}
