package com.training.helpdesk.controller;

import com.training.helpdesk.dto.history.HistoryDto;
import com.training.helpdesk.model.History;
import com.training.helpdesk.service.HistoryService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "/tickets/{ticketId}/histories")
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @PostMapping
    public ResponseEntity<History> save(@Valid @RequestBody HistoryDto historyDto, @PathVariable("ticketId") Long id,
                                        @RequestParam(value = "previousStatus", defaultValue = "") String previousStatus,
                                        @RequestParam(value = "newStatus", defaultValue = "") String newStatus) {
        History savedHistory = historyService.save(historyDto, id, previousStatus, newStatus);

        String currentUri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        String savedHistoryLocation = currentUri + "/" + savedHistory.getId();

        return ResponseEntity.status(CREATED)
                .header(HttpHeaders.LOCATION, savedHistoryLocation)
                .body(savedHistory);
    }

    @GetMapping
    public ResponseEntity<List<HistoryDto>> getAllByTicketId(@PathVariable("ticketId") Long ticketId,
                                                             @RequestParam(value = "buttonValue", defaultValue = "default") String buttonValue) {
        List<HistoryDto> histories = historyService.getAllByTicketId(ticketId, buttonValue);

        return ResponseEntity.ok(histories);
    }
}
