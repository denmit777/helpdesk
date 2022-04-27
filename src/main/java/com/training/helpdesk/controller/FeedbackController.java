package com.training.helpdesk.controller;

import com.training.helpdesk.dto.feedback.FeedbackDto;
import com.training.helpdesk.model.Feedback;
import com.training.helpdesk.service.FeedbackService;
import com.training.helpdesk.service.ValidationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "/tickets/{ticketId}/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final ValidationService validationService;

    public FeedbackController(FeedbackService feedbackService, ValidationService validationService) {
        this.feedbackService = feedbackService;
        this.validationService = validationService;
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody FeedbackDto feedbackDto,
                                  @PathVariable("ticketId") Long ticketId,
                                  BindingResult bindingResult,
                                  Authentication authentication) {
        List<String> errors = validationService.generateErrorMessage(bindingResult);
        if (checkErrors(errors)) {
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        validationService.checkAccessToFeedbackTicket(authentication.getName(), ticketId);

        Feedback savedFeedback = feedbackService.save(feedbackDto, ticketId);

        String currentUri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        String savedFeedbackLocation = currentUri + "/" + savedFeedback.getId();

        return ResponseEntity.status(CREATED)
                .header(HttpHeaders.LOCATION, savedFeedbackLocation)
                .body(savedFeedback);
    }

    @GetMapping
    public ResponseEntity<List<FeedbackDto>> getAllByTicketId(@PathVariable("ticketId") Long ticketId,
                                                              @RequestParam(value = "buttonValue", defaultValue = "default") String buttonValue) {
        List<FeedbackDto> feedbacks = feedbackService.getAllByTicketId(ticketId, buttonValue);

        return ResponseEntity.ok(feedbacks);
    }

    private boolean checkErrors(List<String> errors) {
        return !errors.isEmpty();
    }
}
