package com.training.helpdesk.service;

import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ValidationService {
    List<String> generateErrorMessage(BindingResult bindingResult);

    String getWrongSearchParameterError(String parameter);

    List<String> validateUploadFile(MultipartFile file);

    String checkAccessToCreateTicket(String login);

    void checkAccessToUpdateTicket(String login, Long ticketId);

    void checkAccessToChangeTicketState(String login, Long ticketId, String newState);

    void checkAccessToFeedbackTicket(String login, Long ticketId);
}
