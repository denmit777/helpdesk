package com.training.helpdesk.converter;

import com.training.helpdesk.dto.ticket.TicketCreationOrEditDto;
import com.training.helpdesk.dto.ticket.TicketForListDto;
import com.training.helpdesk.dto.ticket.TicketOverviewDto;
import com.training.helpdesk.model.Ticket;

public interface TicketConverter {

    TicketForListDto convertToDto(Ticket ticket);

    TicketOverviewDto convertToTicketOverviewDto(Ticket ticket);

    Ticket fromTicketCreationOrEditDto(TicketCreationOrEditDto ticketDTO);
}
