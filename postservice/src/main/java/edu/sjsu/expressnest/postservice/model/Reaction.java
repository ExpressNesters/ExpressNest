package edu.sjsu.expressnest.postservice.model;

import java.util.Date;

import edu.sjsu.expressnest.postservice.util.ReactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "en_reactions")
public class Reaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reaction_seq_gen")
    @SequenceGenerator(name = "reaction_seq_gen", sequenceName = "en_reaction_id_seq", allocationSize = 1)
	private long reactionId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "reaction_type")
	private ReactionType reactionType;
	
	@Column(name = "created_at")
	private Date createdAt;
	
	@Column(name = "updated_at")
	private Date updatedAt;
	
	@Column(name = "deleted_at")
	private Date deletedAt;
	
	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;
	
	@Column(name="user_id")
	private long userId;

}
