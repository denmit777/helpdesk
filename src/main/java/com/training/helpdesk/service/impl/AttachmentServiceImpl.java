package com.training.helpdesk.service.impl;

import com.training.helpdesk.converter.AttachmentConverter;
import com.training.helpdesk.dao.AttachmentDAO;
import com.training.helpdesk.dto.attachment.AttachmentDto;
import com.training.helpdesk.model.Attachment;
import com.training.helpdesk.service.AttachmentService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentDAO attachmentDAO;
    private final AttachmentConverter attachmentConverter;

    public AttachmentServiceImpl(AttachmentDAO attachmentDAO, AttachmentConverter attachmentConverter) {
        this.attachmentDAO = attachmentDAO;
        this.attachmentConverter = attachmentConverter;
    }

    @Override
    @Transactional
    public Attachment save(@NonNull AttachmentDto attachmentDto, Long ticketId) {
        Attachment attachment = attachmentConverter.fromAttachmentDto(attachmentDto, ticketId);

        attachmentDAO.save(attachment);

        return attachment;
    }

    @Override
    @Transactional
    public AttachmentDto getById(Long attachmentId, Long ticketId) {
        return attachmentConverter.convertToAttachmentDto(attachmentDAO.getByAttachmentIdAndTicketId(attachmentId, ticketId));
    }

    @Override
    @Transactional
    public List<AttachmentDto> getAllByTicketId(Long ticketId) {
        List<Attachment> attachments = attachmentDAO.getAllByTicketId(ticketId);

        return attachments.stream()
                .map(attachmentConverter::convertToAttachmentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(Long attachmentId, Long ticketId) {
        attachmentDAO.deleteByAttachmentIdAndTicketId(attachmentId, ticketId);
    }
}
