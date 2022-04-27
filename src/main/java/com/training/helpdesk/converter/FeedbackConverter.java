package com.training.helpdesk.converter;

import com.training.helpdesk.dto.feedback.FeedbackDto;
import com.training.helpdesk.model.Feedback;

public interface FeedbackConverter {

    FeedbackDto convertToFeedbackDto(Feedback feedback);

    Feedback fromFeedbackDto(FeedbackDto feedbackDto, Long ticketId);
}
