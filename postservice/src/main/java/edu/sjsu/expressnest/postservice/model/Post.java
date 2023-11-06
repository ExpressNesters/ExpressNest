package edu.sjsu.expressnest.postservice.model;

import java.util.Date;
import java.util.List;

import edu.sjsu.expressnest.postservice.util.PrivacyType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name = "en_posts")
public class Post {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_seq_gen")
    @SequenceGenerator(name = "post_seq_gen", sequenceName = "en_post_id_seq", allocationSize = 1)
	@Column(name="post_id")
	private long postId;
	
	@Column(name="user_id")
	private long userId;
	
	@Column(name="post_text")
	private String postText;
	
	@Column(name="privacy")
	private PrivacyType privacy;
	
	@Column(name="created_at")
	private Date createdAt;
	
	@Column(name="updated_at")
	private Date updatedAt;
	
	@Column(name="deleted_at")
	private Date deletedAt;
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	private List<Attachment> attachments;
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	private List<Comment> comments;
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	private List<Reaction> reactions;

}
