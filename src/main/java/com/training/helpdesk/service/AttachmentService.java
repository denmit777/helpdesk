package com.training.helpdesk.service;

import com.training.helpdesk.dto.attachment.AttachmentDto;
import com.training.helpdesk.model.Attachment;

import java.util.List;

public interface AttachmentService {

    Attachment save(AttachmentDto attachmentDto, Long ticketId);

    AttachmentDto getById(Long attachmentId, Long ticketId);

    List<AttachmentDto> getAllByTicketId(Long ticketId);

    void deleteById(Long attachmentId, Long ticketId);
}
