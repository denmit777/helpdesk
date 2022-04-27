package com.training.helpdesk.mail.service;

import com.training.helpdesk.model.enums.State;

public interface EmailGenerateService {

    void generateEmail(Long ticketId, State previousState, State newState);
}
