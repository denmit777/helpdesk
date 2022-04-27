package com.training.helpdesk.converter.impl;

import com.training.helpdesk.converter.FeedbackConverter;
import com.training.helpdesk.dao.TicketDAO;
import com.training.helpdesk.dto.feedback.FeedbackDto;
import com.training.helpdesk.model.Feedback;
import com.training.helpdesk.model.Ticket;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class FeedbackConverterImpl implements FeedbackConverter {

    private final TicketDAO ticketDAO;

    public FeedbackConverterImpl(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

    @Override
    public FeedbackDto convertToFeedbackDto(Feedback feedback) {
        FeedbackDto feedbackDto = new FeedbackDto();

        feedbackDto.setTicketId(feedback.getTicket().getId());
        feedbackDto.setTicketName(feedback.getTicket().getName());
        feedbackDto.setRate(feedback.getRate());
        feedbackDto.setDate(feedback.getDate());
        feedbackDto.setText(feedback.getText());

        return feedbackDto;
    }

    @Override
    public Feedback fromFeedbackDto(FeedbackDto feedbackDto, Long ticketId) {
        Feedback feedback = new Feedback();

        Ticket ticket = ticketDAO.getById(ticketId);

        feedback.setDate(LocalDateTime.now());
        feedback.setTicket(ticket);
        feedback.setRate(feedbackDto.getRate());
        feedback.setText(feedbackDto.getText());

        return feedback;
    }
}
