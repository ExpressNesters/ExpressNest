package edu.sjsu.expressnest.postservice.service;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.sjsu.expressnest.postservice.dto.AttachmentDTO;
import edu.sjsu.expressnest.postservice.exception.ResourceNotFoundException;
import edu.sjsu.expressnest.postservice.mapper.impl.AttachmentMapper;
import edu.sjsu.expressnest.postservice.model.Attachment;
import edu.sjsu.expressnest.postservice.repository.AttachmentRepository;
import edu.sjsu.expressnest.postservice.util.AttachmentType;
import edu.sjsu.expressnest.postservice.util.PostServiceConstants;

@Service
public class AttachmentService {
	
	@Autowired
	private AttachmentMapper attachmentMapper;
	
	@Autowired
	private AmazonS3Service amazonS3Service;
	
	@Autowired
	private AttachmentRepository attachmentRepository;
	
	@Autowired
	private MessageService messageService;
	
	public Attachment createAttachment(MultipartFile multipartFile) throws IOException {
		String filename = amazonS3Service.uploadFile(multipartFile);
		return Attachment.builder()
				.attachmentType(AttachmentType.Media)
				.attachmentRef(filename).build();
	}
	
	public AttachmentDTO getAttachmentByAttachmentId(long attachmentId) throws ResourceNotFoundException {
		return attachmentRepository.findById(attachmentId)
		.map(attachment -> {
			AttachmentDTO attachmentDTO = attachmentMapper.toAttachmentDTO(attachment);
			URL attachmentURL = amazonS3Service.generatePresignedUrl(attachment.getAttachmentRef(), 3);
			attachmentDTO.setUrl(attachmentURL);
			return attachmentDTO;
		})
		.orElseThrow(() -> new ResourceNotFoundException(
				messageService.getMessage(PostServiceConstants.ATTACHMENT_NOT_FOUND_ERROR_KEY, attachmentId)));
	}
	
	public List<AttachmentDTO> getAttachmentsByPostId(long postId) {
		List<Attachment> attachments = attachmentRepository.findByPost_PostId(postId);
		
		return attachments.stream()
		.map(attachment -> {
			AttachmentDTO attachmentDTO = attachmentMapper.toAttachmentDTO(attachment);
			URL attachmentURL = amazonS3Service.generatePresignedUrl(attachment.getAttachmentRef(), 3);
			attachmentDTO.setUrl(attachmentURL);
			return attachmentDTO;
		})
		.collect(Collectors.toList());
	}
	
	public void deleteAttachmentByAttachmentId(long attachmentId) {
		attachmentRepository.findById(attachmentId)
		.ifPresentOrElse(attachment -> {
			attachment.setDeletedAt(new Date());
			amazonS3Service.deleteFile(attachment.getAttachmentRef());
		}, () -> new ResourceNotFoundException(
				messageService.getMessage(PostServiceConstants.ATTACHMENT_NOT_FOUND_ERROR_KEY, attachmentId)));
	}
	
}
