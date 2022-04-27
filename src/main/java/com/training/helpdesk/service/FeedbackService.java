package com.training.helpdesk.service;

import com.training.helpdesk.dto.feedback.FeedbackDto;;
import com.training.helpdesk.model.Feedback;

import java.util.List;

public interface FeedbackService {

    Feedback save(FeedbackDto feedbackDto, Long ticketId);

    List<FeedbackDto> getAllByTicketId(Long ticketId, String buttonValue);
}
