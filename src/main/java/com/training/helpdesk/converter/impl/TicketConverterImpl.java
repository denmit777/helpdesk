package com.training.helpdesk.converter.impl;

import com.training.helpdesk.converter.TicketConverter;
import com.training.helpdesk.dto.ticket.TicketCreationOrEditDto;
import com.training.helpdesk.dto.ticket.TicketForListDto;
import com.training.helpdesk.dto.ticket.TicketOverviewDto;
import com.training.helpdesk.model.Ticket;
import com.training.helpdesk.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class TicketConverterImpl implements TicketConverter {

    @Override
    public TicketForListDto convertToDto(Ticket ticket) {
        TicketForListDto ticketForListDto = new TicketForListDto();

        ticketForListDto.setId(ticket.getId());
        ticketForListDto.setName(ticket.getName());
        ticketForListDto.setDesiredResolutionDate(ticket.getDesiredResolutionDate());
        ticketForListDto.setUrgency(ticket.getUrgency());
        ticketForListDto.setStatus(ticket.getState());

        return ticketForListDto;
    }

    @Override
    public TicketOverviewDto convertToTicketOverviewDto(Ticket ticket) {
        TicketOverviewDto ticketOverviewDto = new TicketOverviewDto();

        ticketOverviewDto.setId(ticket.getId());
        ticketOverviewDto.setName(ticket.getName());
        ticketOverviewDto.setCreatedOn(LocalDate.now());
        ticketOverviewDto.setStatus(ticket.getState());
        ticketOverviewDto.setUrgency(ticket.getUrgency());
        ticketOverviewDto.setDesiredResolutionDate(ticket.getDesiredResolutionDate());
        ticketOverviewDto.setTicketOwner(getUserEmail(ticket.getTicketOwner()));
        ticketOverviewDto.setApprover(getUserEmail(ticket.getApprover()));
        ticketOverviewDto.setAssignee(getUserEmail(ticket.getAssignee()));
        ticketOverviewDto.setDescription(ticket.getDescription());
        ticketOverviewDto.setCategory(ticket.getCategory().getName());

        return ticketOverviewDto;
    }

    @Override
    public Ticket fromTicketCreationOrEditDto(TicketCreationOrEditDto ticketDTO) {
        Ticket ticket = new Ticket();

        ticket.setName(ticketDTO.getName());
        ticket.setDescription(ticketDTO.getDescription());
        ticket.setDesiredResolutionDate(ticketDTO.getDesiredResolutionDate());
        ticket.setCategory(ticketDTO.getCategory());
        ticket.setUrgency(ticketDTO.getUrgency());

        return ticket;
    }

    private Optional<String> getUserEmail(User user) {
        if (user != null) {
            return Optional.of(user.getEmail());
        }
        return Optional.empty();
    }
}
