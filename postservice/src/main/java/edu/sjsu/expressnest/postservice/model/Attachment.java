package edu.sjsu.expressnest.postservice.model;

import java.util.Date;

import edu.sjsu.expressnest.postservice.util.AttachmentType;
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
@Table(name = "en_attachments")
public class Attachment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attachment_seq_gen")
    @SequenceGenerator(name = "attachment_seq_gen", sequenceName = "en_attachment_id_seq", allocationSize = 1)
	private long attachmentId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "attachment_type")
	private AttachmentType attachmentType;
	
	@Column(name = "attachment_ref")
	private String attachmentRef;
	
	@Column(name="created_at")
	private Date createdAt;
	
	@Column(name="updated_at")
	private Date updatedAt;
	
	@Column(name="deleted_at")
	private Date deletedAt;
	
	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;
	
	@Column(name="user_id")
	private long userId;

}
