package com.training.helpdesk.converter;

import com.training.helpdesk.dto.attachment.AttachmentDto;
import com.training.helpdesk.model.Attachment;

public interface AttachmentConverter {

    AttachmentDto convertToAttachmentDto(Attachment attachment);

    Attachment fromAttachmentDto(AttachmentDto attachmentDto, Long ticketId);
}
