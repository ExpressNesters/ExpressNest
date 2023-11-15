package edu.sjsu.expressnest.postservice.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.sjsu.expressnest.postservice.mapper.impl.AttachmentMapper;
import edu.sjsu.expressnest.postservice.model.Attachment;
import edu.sjsu.expressnest.postservice.repository.AttachmentRepository;
import edu.sjsu.expressnest.postservice.util.AttachmentType;

@Service
public class AttachmentService {
	
	@Autowired
	private AttachmentMapper attachmentMapper;
	
	@Autowired
	private AmazonS3Service amazonS3Service;
	
	@Autowired
	private AttachmentRepository attachmentRepository;
	
	public Attachment createAttachment(MultipartFile multipartFile) throws IOException {
		String filename = uploadFileToS3(multipartFile);
		return Attachment.builder()
				.attachmentType(AttachmentType.Media)
				.attachmentRef(filename).build();
	}
	
	public String uploadFileToS3(MultipartFile multipartFile) throws IOException {
		String fileName = amazonS3Service.uploadFile(multipartFile);
		return fileName;
	}
	
	public String downloadFile(String bucketName, String filename) {
		return null;
	}
	
	public String deleteFile(String bucketName, String filename) {
		return null;
	}
}
