package com.training.helpdesk.mail.service.impl;

import com.training.helpdesk.dao.TicketDAO;
import com.training.helpdesk.dao.UserDAO;
import com.training.helpdesk.model.User;
import com.training.helpdesk.mail.service.EmailService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.util.*;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${mail.username}")
    private String sendFrom;

    @Value("${permitted.url}")
    private String baseUrl;

    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;

    private static final String TICKET_ID = "ticketId";
    private static final String BASE_URL = "baseUrl";
    private static final String USER_NAME = "username";
    private static final String USER_LASTNAME = "userSurname";

    private final JavaMailSender emailSender;
    private final UserDAO userDAO;
    private final TicketDAO ticketDAO;

    public EmailServiceImpl(JavaMailSender emailSender, UserDAO userDAO, TicketDAO ticketDAO) {
        this.emailSender = emailSender;
        this.userDAO = userDAO;
        this.ticketDAO = ticketDAO;
    }

    @Override
    public void sendMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel, String template) {
        Context thymeleafContext = new Context();

        thymeleafContext.setVariables(templateModel);

        String htmlBody = thymeleafTemplateEngine.process(template, thymeleafContext);

        sendHtmlMessage(to, subject, htmlBody);
    }

    @SneakyThrows
    private void sendHtmlMessage(String to, String subject, String htmlBody) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(sendFrom);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        emailSender.send(message);
    }

    @Override
    public void sendNewTicketMail(Long ticketId) {
        List<User> recipients = userDAO.getAllManagers();

        recipients.forEach(recipient -> sendMessageUsingThymeleafTemplate(recipient.getEmail(), "New ticket for approval",
                Map.of(
                        TICKET_ID, ticketId,
                        BASE_URL, baseUrl
                ), "template#1-new.html"));
    }

    @Override
    public void sendApprovedTicketMail(Long ticketId) {
        List<User> recipients = userDAO.getAllEngineers();

        recipients.add(ticketDAO.getById(ticketId).getTicketOwner());

        recipients.forEach(recipient -> sendMessageUsingThymeleafTemplate(recipient.getEmail(), "Ticket was approved",
                Map.of(
                        TICKET_ID, ticketId,
                        BASE_URL, baseUrl
                ), "template#2-approved.html"));
    }

    @Override
    public void sendDeclinedTicketMail(Long ticketId) {
        User recipient = ticketDAO.getById(ticketId).getTicketOwner();

        sendMessageUsingThymeleafTemplate(recipient.getEmail(), "Ticket was declined",
                Map.of(
                        TICKET_ID, ticketId,
                        USER_NAME, getRecipientFirstName(recipient),
                        USER_LASTNAME, getRecipientLastName(recipient),
                        BASE_URL, baseUrl
                ), "template#3-declined.html");
    }

    @Override
    public void sendNewCancelledTicketMail(Long ticketId) {
        User recipient = ticketDAO.getById(ticketId).getTicketOwner();

        sendMessageUsingThymeleafTemplate(recipient.getEmail(), "Ticket was cancelled",
                Map.of(
                        TICKET_ID, ticketId,
                        USER_NAME, getRecipientFirstName(recipient),
                        USER_LASTNAME, getRecipientLastName(recipient),
                        BASE_URL, baseUrl
                ), "template#4-new-cancelled.html");
    }

    @Override
    public void sendApprovedCancelledTicketMail(Long ticketId) {
        List<User> recipients = new ArrayList<>();

        recipients.add(ticketDAO.getById(ticketId).getTicketOwner());
        recipients.add(ticketDAO.getById(ticketId).getApprover());

        recipients.forEach(recipient -> sendMessageUsingThymeleafTemplate(recipient.getEmail(), "Ticket was cancelled",
                Map.of(
                        TICKET_ID, ticketId,
                        BASE_URL, baseUrl
                ), "template#5-approved-cancelled.html"));
    }

    @Override
    public void sendDoneTicketMail(Long ticketId) {
        User recipient = ticketDAO.getById(ticketId).getTicketOwner();

        sendMessageUsingThymeleafTemplate(recipient.getEmail(), "Ticket was done",
                Map.of(
                        TICKET_ID, ticketId,
                        USER_NAME, getRecipientFirstName(recipient),
                        USER_LASTNAME, getRecipientLastName(recipient),
                        BASE_URL, baseUrl
                ), "template#6-done.html");
    }

    @Override
    public void sendFeedback(Long ticketId) {
        User recipient = ticketDAO.getById(ticketId).getAssignee();

        sendMessageUsingThymeleafTemplate(recipient.getEmail(), "Feedback was provided",
                Map.of(
                        TICKET_ID, ticketId,
                        USER_NAME, getRecipientFirstName(recipient),
                        USER_LASTNAME, getRecipientLastName(recipient),
                        BASE_URL, baseUrl
                ), "template#7-feedback.html");
    }

    private String getRecipientFirstName(User recipient) {
        if (recipient.getFirstName() != null) {
            return recipient.getFirstName();
        }
        return recipient.getEmail();
    }

    private String getRecipientLastName(User recipient) {
        if (recipient.getLastName() != null) {
            return recipient.getLastName();
        }
        return "";
    }
}

