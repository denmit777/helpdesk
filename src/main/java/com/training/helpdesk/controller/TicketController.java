package com.training.helpdesk.controller;

import com.training.helpdesk.dto.ticket.TicketCreationOrEditDto;
import com.training.helpdesk.dto.ticket.TicketForListDto;
import com.training.helpdesk.dto.ticket.TicketOverviewDto;
import com.training.helpdesk.model.Ticket;
import com.training.helpdesk.model.enums.State;
import com.training.helpdesk.service.TicketService;
import com.training.helpdesk.service.ValidationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final ValidationService validationService;

    public TicketController(TicketService ticketService, ValidationService validationService) {
        this.ticketService = ticketService;
        this.validationService = validationService;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid TicketCreationOrEditDto ticketCreationDto,
                                  Authentication authentication,
                                  @RequestParam(value = "buttonValue", defaultValue = "default") String buttonValue) {

        String errorAccessCreateTicketMessage = validationService.checkAccessToCreateTicket(authentication.getName());

        if (checkErrors(errorAccessCreateTicketMessage)) {
            return new ResponseEntity<>(errorAccessCreateTicketMessage, HttpStatus.FORBIDDEN);
        }

        Ticket savedTicket = ticketService.save(ticketCreationDto, authentication.getName(), buttonValue);

        String currentUri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        String savedTicketLocation = currentUri + "/" + savedTicket.getId();

        return ResponseEntity.status(CREATED)
                .header(HttpHeaders.LOCATION, savedTicketLocation)
                .body(savedTicket);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketOverviewDto> getById(@PathVariable("id") Long id) {
        TicketOverviewDto ticketDto = ticketService.getById(id);

        return ResponseEntity.ok(ticketDto);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll(Authentication authentication,
                                    @RequestParam(value = "searchField", defaultValue = "default") String searchField,
                                    @RequestParam(value = "parameter", defaultValue = "") String parameter,
                                    @RequestParam(value = "sortField", defaultValue = "default") String sortField,
                                    @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection,
                                    @RequestParam(value = "pageSize", defaultValue = "25") int pageSize,
                                    @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber) {

        String errorMessage = validationService.getWrongSearchParameterError(parameter);

        if (checkErrors(errorMessage)) {
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        List<TicketForListDto> tickets = ticketService.getAll(authentication.getName(), searchField, parameter, sortField,
                sortDirection, pageSize, pageNumber);

        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMy(Authentication authentication,
                                   @RequestParam(value = "searchField", defaultValue = "default") String searchField,
                                   @RequestParam(value = "parameter", defaultValue = "") String parameter,
                                   @RequestParam(value = "sortField", defaultValue = "default") String sortField,
                                   @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection,
                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                   @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber) {

        String errorMessage = validationService.getWrongSearchParameterError(parameter);

        if (checkErrors(errorMessage)) {
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        List<TicketForListDto> tickets = ticketService.getMy(authentication.getName(), searchField, parameter, sortField,
                sortDirection, pageSize, pageNumber);

        return ResponseEntity.ok(tickets);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(Authentication authentication,
                                    @PathVariable("id") Long ticketId,
                                    @Valid @RequestBody TicketCreationOrEditDto ticketEditDto,
                                    @RequestParam(value = "buttonValue", defaultValue = "default") String buttonValue) {

        validationService.checkAccessToUpdateTicket(authentication.getName(), ticketId);

        Ticket updatedTicket = ticketService.update(ticketId, ticketEditDto, authentication.getName(), buttonValue);

        return ResponseEntity.ok(updatedTicket);
    }

    @PutMapping("/{id}/change-status")
    public ResponseEntity<?> changeTicketStatus(Authentication authentication,
                                                @PathVariable("id") Long ticketId,
                                                @RequestParam(value = "newStatus", required = false) String newState) {
        validationService.checkAccessToChangeTicketState(authentication.getName(), ticketId, newState);

        ticketService.changeState(authentication.getName(), ticketId, State.valueOf(newState.toUpperCase()));

        return ResponseEntity.created(URI.create(String.format("/tickets/change-status/%s", ticketId))).build();
    }

    @GetMapping
    public ResponseEntity<String> getNewTicketId() {
        List<TicketForListDto> tickets = ticketService.getTotalAmount();

        String newTicketId = String.valueOf(tickets.size() + 1);

        return ResponseEntity.ok(newTicketId);
    }

    private boolean checkErrors(String errorMessage) {
        return !errorMessage.isEmpty();
    }
}
