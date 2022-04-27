package com.training.helpdesk.converter.impl;

import com.training.helpdesk.converter.CommentConverter;
import com.training.helpdesk.dao.TicketDAO;
import com.training.helpdesk.dto.comment.CommentDto;
import com.training.helpdesk.model.Comment;
import com.training.helpdesk.model.Ticket;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CommentConverterImpl implements CommentConverter {

    private final TicketDAO ticketDAO;

    public CommentConverterImpl(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

    @Override
    public CommentDto convertToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();

        commentDto.setDate(comment.getDate());
        commentDto.setUser(comment.getUser().getEmail());
        commentDto.setText(comment.getText());

        return commentDto;
    }

    @Override
    public Comment fromCommentDto(CommentDto commentDto, Long ticketId) {
        Comment comment = new Comment();

        Ticket ticket = ticketDAO.getById(ticketId);

        comment.setDate(LocalDateTime.now());
        comment.setUser(ticket.getTicketOwner());
        comment.setTicket(ticket);
        comment.setText(commentDto.getText());

        return comment;
    }
}
