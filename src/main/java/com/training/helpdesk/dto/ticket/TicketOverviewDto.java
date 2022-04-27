package com.training.helpdesk.dto.ticket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.training.helpdesk.model.enums.State;
import com.training.helpdesk.model.enums.Urgency;
import lombok.*;

import java.time.LocalDate;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketOverviewDto {

    private Long id;

    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createdOn;

    private State status;

    private Urgency urgency;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate desiredResolutionDate;

    private Optional ticketOwner;

    private Optional approver;

    private Optional assignee;

    private String description;

    private String category;
}
