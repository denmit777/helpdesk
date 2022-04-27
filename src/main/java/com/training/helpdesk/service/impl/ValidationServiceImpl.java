package com.training.helpdesk.service.impl;

import com.training.helpdesk.dao.TicketDAO;
import com.training.helpdesk.dao.UserDAO;
import com.training.helpdesk.exception.*;
import com.training.helpdesk.model.Ticket;
import com.training.helpdesk.model.User;
import com.training.helpdesk.model.enums.Role;
import com.training.helpdesk.model.enums.State;
import com.training.helpdesk.service.ValidationService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.training.helpdesk.model.enums.Role.*;
import static com.training.helpdesk.model.enums.State.*;

@Service
public class ValidationServiceImpl implements ValidationService {

    private static final String DOWNLOADABLE_FILE_FORMAT = "jpg|pdf|doc|docx|png|jpeg";
    private static final String DOWNLOADABLE_FILE_FORMAT_ERROR_MESSAGE = "The selected file type is not allowed. Please select a file of " +
            "one of the following types: pdf, png, doc, docx, jpg, jpeg.";
    private static final Double ALLOWED_MAXIMUM_SIZE = 5.0;
    private static final String ALLOWED_MAXIMUM_SIZE_ERROR_MESSAGE = "The size of the attached file should not be greater than 5 Mb. " +
            "Please select another file.";

    private static final Map<Role, Set<State>> ACCESS_TO_CHANGE_STATE = Map.of(
            ROLE_EMPLOYEE, Set.of(NEW, CANCELED),
            ROLE_MANAGER, Set.of(NEW, CANCELED, APPROVED, DECLINED),
            ROLE_ENGINEER, Set.of(IN_PROGRESS, DONE, CANCELED)
    );

    private final TicketDAO ticketDAO;
    private final UserDAO userDAO;

    public ValidationServiceImpl(TicketDAO ticketDAO, UserDAO userDAO) {
        this.ticketDAO = ticketDAO;
        this.userDAO = userDAO;
    }

    @Bean(name = "multipartResolver")
    public MultipartResolver getMultipartResolver() {
        return new CommonsMultipartResolver();
    }

    @Override
    public List<String> generateErrorMessage(BindingResult bindingResult) {
        return Optional.of(bindingResult)
                .filter(BindingResult::hasErrors)
                .map(this::getErrors)
                .orElseGet(ArrayList::new);
    }

    private List<String> getErrors(BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors();

        return errors.stream()
                .map(e -> e.getField() + " : " + e.getDefaultMessage())
                .collect(Collectors.toList());
    }

    @Override
    public String getWrongSearchParameterError(String parameter) {
        Pattern pattern = Pattern.compile("[а-яёА-ЯЁ]+");
        Matcher matcher = pattern.matcher(parameter);

        if (!(parameter.equals("")) && matcher.find()) {
            return "Wrong search parameter";
        }
        return "";
    }

    @Override
    public List<String> validateUploadFile(MultipartFile file) {
        List<String> fileUploadErrors = new ArrayList<>();

        if (file != null) {
            if (!getFileExtension(file).matches(DOWNLOADABLE_FILE_FORMAT)) {
                fileUploadErrors.add(DOWNLOADABLE_FILE_FORMAT_ERROR_MESSAGE);
            }

            if (getFileSizeMegaBytes(file) > ALLOWED_MAXIMUM_SIZE) {
                fileUploadErrors.add(ALLOWED_MAXIMUM_SIZE_ERROR_MESSAGE);
            }
        }
        return fileUploadErrors;
    }

    @Override
    public String checkAccessToCreateTicket(String login) {
        User user = userDAO.getByLogin(login);
        if (user.getRole().equals(ROLE_ENGINEER)) {
            return ("You can't have access to create ticket");
        }
        return "";
    }

    @Override
    public void checkAccessToUpdateTicket(String login, Long ticketId) {
        User user = userDAO.getByLogin(login);
        ticketDAO.checkAccessToDraftTicket(user.getId(), ticketId)
                .orElseThrow(() -> new NoAccessToTicketException("You can't have access to update ticket"));
    }

    @Override
    public void checkAccessToChangeTicketState(String login, Long ticketId, String newState) {
        User user = userDAO.getByLogin(login);

        Ticket ticket = ticketDAO.getById(ticketId);

        if (ticket.getTicketOwner().getEmail().equals(user.getEmail()) && ticket.getState() != DRAFT) {
            throw new NoAccessToTicketException("You can't formatted your own ticket");
        }

        State state = getStateFromString(newState);

        boolean thereIsAccess = ACCESS_TO_CHANGE_STATE.get(user.getRole()).stream()
                .anyMatch(states -> states == state);

        if (!thereIsAccess) {
            throw new NoAccessToChangeTicketState("You can't change state current ticket");
        }
    }

    @Override
    public void checkAccessToFeedbackTicket(String login, Long ticketId) {
        User user = userDAO.getByLogin(login);

        ticketDAO.checkAccessToFeedbackTicket(user.getId(), ticketId)
                .orElseThrow(() -> new NoAccessToTicketException("You don't have access to provide feedback for this ticket"));
    }

    private State getStateFromString(String newState) {
        try {
            if (newState.contains("-")) {
                newState = newState.replace("-", "_");
            }
            return State.valueOf(newState.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new StateNotFoundException(String.format("This state \"%s\" does not exist", newState));
        }
    }

    private String getFileExtension(MultipartFile file) {
        if (file == null) {
            return "";
        }
        String fileName = file.getOriginalFilename();
        assert fileName != null;

        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    private double getFileSizeMegaBytes(MultipartFile file) {
        return (double) file.getSize() / (1024 * 1024);
    }
}
