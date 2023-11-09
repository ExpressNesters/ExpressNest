package edu.sjsu.expressnest.postservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.expressnest.postservice.dto.AttachmentDTO;
import edu.sjsu.expressnest.postservice.exception.ResourceNotFoundException;
import edu.sjsu.expressnest.postservice.service.AttachmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/attachment")
@Validated
public class AttachmentController {
	
	@Autowired
	AttachmentService attachmentService;
	
	@GetMapping("/getAttachmentByAttachmentId")
	public ResponseEntity<AttachmentDTO> getAttachmentByAttachmentId(@NotNull @Positive @RequestParam long attachmentId) throws ResourceNotFoundException {
		return null;
	}
	
	@GetMapping("/getAttachmentsByPostId")
	public ResponseEntity<List<AttachmentDTO>> getAttachmentsByPostId(@NotNull @Positive @RequestParam long postId) throws ResourceNotFoundException {
		return null;
	}
	
	@PostMapping("/createAttachment")
	public ResponseEntity<AttachmentDTO> createAttachment(@Valid @RequestBody AttachmentDTO attachmentDTO) {
		return null;
	}
	
	@PostMapping("/updateAttachment")
	public ResponseEntity<AttachmentDTO> updateAttachment(@RequestBody AttachmentDTO attachmentDTO) {
		return null;
	}
	
	@PostMapping("/deleteAttachment")
	public ResponseEntity<String> deleteAttachment(@NotNull @Positive @RequestBody long attachmentId) throws ResourceNotFoundException {
		return null;
	}

}
