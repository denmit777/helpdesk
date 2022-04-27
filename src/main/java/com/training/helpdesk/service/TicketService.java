package com.training.helpdesk.service;

import com.training.helpdesk.dto.ticket.TicketCreationOrEditDto;
import com.training.helpdesk.dto.ticket.TicketForListDto;
import com.training.helpdesk.dto.ticket.TicketOverviewDto;
import com.training.helpdesk.model.Ticket;
import com.training.helpdesk.model.enums.State;

import java.util.List;

public interface TicketService {

    Ticket save(TicketCreationOrEditDto ticketCreationDto, String login, String buttonValue);

    TicketOverviewDto getById(Long id);

    Ticket update(Long id, TicketCreationOrEditDto ticketEditDto, String login, String buttonValue);

    List<TicketForListDto> getAll(String login, String searchField, String parameter, String sortField,
                                  String sortDirection, int pageSize, int pageNumber);

    List<TicketForListDto> getMy(String login, String searchField, String parameter, String sortField,
                                 String sortDirection, int pageSize, int pageNumber);

    List<TicketForListDto> getTotalAmount();

    void changeState(String login, Long ticketId, State newState);
}
