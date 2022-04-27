package com.training.helpdesk.mail.service;

import java.util.Map;

public interface EmailService {

    void sendMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel, String template);

    void sendNewTicketMail(Long ticketId);

    void sendApprovedTicketMail(Long ticketId);

    void sendNewCancelledTicketMail(Long ticketId);

    void sendDeclinedTicketMail(Long ticketId);

    void sendApprovedCancelledTicketMail(Long ticketId);

    void sendDoneTicketMail(Long ticketId);

    void sendFeedback(Long ticketId);
}
