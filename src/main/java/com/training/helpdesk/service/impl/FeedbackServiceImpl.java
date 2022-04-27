package com.training.helpdesk.service.impl;

import com.training.helpdesk.converter.FeedbackConverter;
import com.training.helpdesk.dao.FeedBackDAO;
import com.training.helpdesk.dto.feedback.FeedbackDto;
import com.training.helpdesk.model.Feedback;
import com.training.helpdesk.mail.service.EmailService;
import com.training.helpdesk.service.FeedbackService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedBackDAO feedBackDAO;
    private final FeedbackConverter feedbackConverter;
    private final EmailService emailService;

    public FeedbackServiceImpl(FeedBackDAO feedBackDAO, FeedbackConverter feedbackConverter, EmailService emailService) {
        this.feedBackDAO = feedBackDAO;
        this.feedbackConverter = feedbackConverter;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public Feedback save(FeedbackDto feedbackDto, Long ticketId) {
        Feedback feedback = feedbackConverter.fromFeedbackDto(feedbackDto, ticketId);

        feedBackDAO.save(feedback);

        emailService.sendFeedback(ticketId);

        return feedback;
    }

    @Override
    @Transactional
    public List<FeedbackDto> getAllByTicketId(Long ticketId, String buttonValue) {
        List<Feedback> feedbacks;

        if (buttonValue.equals("Show All")) {
            feedbacks = feedBackDAO.getAllByTicketId(ticketId);
        } else {
            feedbacks = feedBackDAO.getLastFiveByTicketId(ticketId);
        }

        return feedbacks.stream()
                .map(feedbackConverter::convertToFeedbackDto)
                .collect(Collectors.toList());
    }
}
