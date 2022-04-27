package com.training.helpdesk.converter.impl;

import com.training.helpdesk.converter.HistoryConverter;
import com.training.helpdesk.dao.TicketDAO;
import com.training.helpdesk.dto.history.HistoryDto;
import com.training.helpdesk.model.History;
import com.training.helpdesk.model.Ticket;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class HistoryConverterImpl implements HistoryConverter {

    private final TicketDAO ticketDAO;

    public HistoryConverterImpl(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

    @Override
    public HistoryDto convertToHistoryDto(History history) {
        HistoryDto historyDto = new HistoryDto();

        historyDto.setDate(history.getDate());
        historyDto.setUser(history.getUser().getEmail());
        historyDto.setAction(history.getAction());
        historyDto.setDescription(history.getDescription());

        return historyDto;
    }

    @Override
    public History fromHistoryDto(HistoryDto historyDto, Long ticketId) {
        History history = new History();

        Ticket ticket = ticketDAO.getById(ticketId);

        history.setDate(LocalDateTime.now());
        history.setUser(ticket.getTicketOwner());
        history.setTicket(ticket);
        history.setAction(historyDto.getAction());
        history.setDescription(historyDto.getDescription());

        return history;
    }
}
