package com.training.helpdesk.converter.impl;

import com.training.helpdesk.converter.AttachmentConverter;
import com.training.helpdesk.dao.TicketDAO;
import com.training.helpdesk.dto.attachment.AttachmentDto;
import com.training.helpdesk.model.Attachment;
import com.training.helpdesk.model.Ticket;
import org.springframework.stereotype.Component;

@Component
public class AttachmentConverterImpl implements AttachmentConverter {

    private final TicketDAO ticketDAO;

    public AttachmentConverterImpl(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

    @Override
    public AttachmentDto convertToAttachmentDto(Attachment attachment) {
        AttachmentDto attachmentDto = new AttachmentDto();

        attachmentDto.setId(attachment.getId());
        attachmentDto.setName(attachment.getName());
        attachmentDto.setFile(attachment.getFile());

        return attachmentDto;
    }

    @Override
    public Attachment fromAttachmentDto(AttachmentDto attachmentDto, Long ticketId) {
        Attachment attachment = new Attachment();

        Ticket ticket = ticketDAO.getById(ticketId);

        attachment.setName(attachmentDto.getName());
        attachment.setFile(attachmentDto.getFile());
        attachment.setTicket(ticket);

        return attachment;
    }
}
