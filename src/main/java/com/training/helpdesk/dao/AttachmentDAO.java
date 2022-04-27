package com.training.helpdesk.dao;

import com.training.helpdesk.model.Attachment;

import java.util.List;

public interface AttachmentDAO {

    void save(Attachment attachment);

    Attachment getByAttachmentIdAndTicketId(Long attachmentId, Long ticketId);

    List<Attachment> getAllByTicketId(Long id);

    void deleteByAttachmentIdAndTicketId(Long attachmentId, Long ticketId);
}
