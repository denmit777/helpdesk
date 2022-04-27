package com.training.helpdesk.mail.service.impl;

import com.training.helpdesk.mail.enums.ChangeStatusMail;
import com.training.helpdesk.model.enums.State;
import com.training.helpdesk.mail.service.EmailGenerateService;
import com.training.helpdesk.mail.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;

import static com.training.helpdesk.mail.enums.ChangeStatusMail.*;

@Service
public class EmailGenerateServiceImpl implements EmailGenerateService {

    private final Map<ChangeStatusMail, Consumer<Long>> sendStatus;

    @Autowired
    public EmailGenerateServiceImpl(EmailService emailService) {
        sendStatus = Map.of(
                DRAFT_NEW, emailService::sendNewTicketMail,
                DECLINED_NEW, emailService::sendNewTicketMail,
                NEW_APPROVED, emailService::sendApprovedTicketMail,
                NEW_CANCELED, emailService::sendNewCancelledTicketMail,
                NEW_DECLINED, emailService::sendDeclinedTicketMail,
                APPROVED_CANCELED, emailService::sendApprovedCancelledTicketMail,
                IN_PROGRESS_DONE, emailService::sendDoneTicketMail
        );
    }

    @Override
    public void generateEmail(Long ticketId, State previousState, State newState) {
        Map<State, State> ticketState = Map.of(previousState, newState);

        Arrays.stream(ChangeStatusMail.values())
                .filter(states -> states.isPreviousStateToCurrentStateMapEqualTo(ticketState))
                .findFirst()
                .ifPresent(state -> sendStatus.get(state).accept(ticketId));
    }
}
