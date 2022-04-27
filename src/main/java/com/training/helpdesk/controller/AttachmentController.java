package com.training.helpdesk.controller;

import com.training.helpdesk.dto.attachment.AttachmentDto;
import com.training.helpdesk.service.AttachmentService;
import com.training.helpdesk.service.ValidationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "/tickets/{ticketId}/attachments")
public class AttachmentController {

    private static final String RESPONSE_ATTACHMENT_HEADER = "attachment; filename=\"%s\"";

    private final AttachmentService attachmentService;
    private final ValidationService validationService;

    public AttachmentController(AttachmentService attachmentService, ValidationService validationService) {
        this.attachmentService = attachmentService;
        this.validationService = validationService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(@RequestParam(value = "fileName", defaultValue = "file") String fileName,
                                  @RequestParam(name = "file", required = false) MultipartFile file,
                                  @PathVariable("ticketId") Long ticketId) throws IOException {
        List<String> fileUploadErrors = validationService.validateUploadFile(file);

        if (checkErrors(fileUploadErrors)) {
            return new ResponseEntity<>(fileUploadErrors, HttpStatus.BAD_REQUEST);
        }

        AttachmentDto attachmentDto = new AttachmentDto();

        attachmentDto.setName(fileName);
        attachmentDto.setFile(file.getBytes());

        attachmentService.save(attachmentDto, ticketId);
        return ResponseEntity.created(URI.create(attachmentDto.getName())).build();
    }

    @GetMapping("/{attachmentId}")
    public ResponseEntity<AttachmentDto> getById(@PathVariable("attachmentId") Long attachmentId,
                                                 @PathVariable("ticketId") Long ticketId) {
        AttachmentDto attachment = attachmentService.getById(attachmentId, ticketId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format(RESPONSE_ATTACHMENT_HEADER, attachment.getName()))
                .body(attachment);
    }

    @GetMapping
    public ResponseEntity<List<AttachmentDto>> getAllByTicketId(@PathVariable("ticketId") Long id) {
        List<AttachmentDto> attachments = attachmentService.getAllByTicketId(id);

        return ResponseEntity.ok(attachments);
    }

    @DeleteMapping("/{attachmentId}")
    public ResponseEntity<Void> delete(@PathVariable("attachmentId") Long attachmentId,
                                       @PathVariable("ticketId") Long ticketId) {
        attachmentService.deleteById(attachmentId, ticketId);

        return ResponseEntity.ok().build();
    }

    private boolean checkErrors(List<String> fileUploadErrors) {
        return !fileUploadErrors.isEmpty();
    }
}
