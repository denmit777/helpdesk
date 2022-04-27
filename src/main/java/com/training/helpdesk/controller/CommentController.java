package com.training.helpdesk.controller;

import com.training.helpdesk.dto.comment.CommentDto;
import com.training.helpdesk.model.Comment;
import com.training.helpdesk.service.CommentService;
import com.training.helpdesk.service.ValidationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "/tickets/{ticketId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final ValidationService validationService;

    public CommentController(CommentService commentService, ValidationService validationService) {
        this.commentService = commentService;
        this.validationService = validationService;
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody CommentDto commentDto,
                                  @PathVariable("ticketId") Long id,
                                  BindingResult bindingResult) {

        List<String> errorMessage = validationService.generateErrorMessage(bindingResult);

        if (checkErrors(errorMessage)) {
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        Comment savedComment = commentService.save(commentDto, id);

        String currentUri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        String savedCommentLocation = currentUri + "/" + savedComment.getId();

        return ResponseEntity.status(CREATED)
                .header(HttpHeaders.LOCATION, savedCommentLocation)
                .body(savedComment);
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllByTicketId(@PathVariable("ticketId") Long ticketId,
                                                             @RequestParam(value = "buttonValue", defaultValue = "default") String buttonValue) {
        List<CommentDto> comments = commentService.getAllByTicketId(ticketId, buttonValue);

        return ResponseEntity.ok(comments);
    }

    private boolean checkErrors(List<String> errorMessage) {
        return !errorMessage.isEmpty();
    }
}
