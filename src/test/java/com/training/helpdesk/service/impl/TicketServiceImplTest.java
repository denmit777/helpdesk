package com.training.helpdesk.service.impl;

import com.training.helpdesk.converter.TicketConverter;
import com.training.helpdesk.dao.TicketDAO;
import com.training.helpdesk.dao.UserDAO;
import com.training.helpdesk.dto.ticket.TicketCreationOrEditDto;
import com.training.helpdesk.dto.ticket.TicketForListDto;
import com.training.helpdesk.dto.ticket.TicketOverviewDto;
import com.training.helpdesk.exception.TicketNotFoundException;
import com.training.helpdesk.model.Ticket;
import com.training.helpdesk.model.User;
import com.training.helpdesk.model.enums.Role;
import com.training.helpdesk.model.enums.State;
import com.training.helpdesk.model.enums.Urgency;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TicketServiceImplTest {
    @Mock
    private TicketDAO ticketDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private TicketConverter ticketConverter;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Test
    public void getAllTicketsTest() {
        final User user = new User();
        user.setRole(Role.ROLE_MANAGER);
        user.setEmail("manager1_mogilev@yopmail.com");
        user.setPassword("P@ssword1");

        Ticket ticketOne = new Ticket();
        ticketOne.setId(1L);
        ticketOne.setName("task 1");
        ticketOne.setDesiredResolutionDate(LocalDate.parse("2022-03-12"));
        ticketOne.setState(State.NEW);
        ticketOne.setUrgency(Urgency.CRITICAL);

        Ticket ticketTwo = new Ticket();
        ticketTwo.setId(4L);
        ticketTwo.setName("new task");
        ticketTwo.setDesiredResolutionDate(LocalDate.parse("2022-04-15"));
        ticketTwo.setState(State.APPROVED);
        ticketTwo.setUrgency(Urgency.AVERAGE);

        List<Ticket> tickets = asList(ticketOne, ticketTwo);
        List<TicketForListDto> expected = tickets.stream()
                .map(ticketConverter::convertToDto)
                .collect(Collectors.toList());

        when(userDAO.getByLogin(user.getEmail())).thenReturn(user);

        when(ticketDAO.getAllForManager(user.getId(), "default",
                "")).thenReturn(tickets);

        List<TicketForListDto> actual = ticketService.getAll(user.getEmail(),
                "default", "",
                "default", "", 10, 1);

        Assert.assertEquals(2, actual.size());
        Assert.assertEquals(2, expected.size());
        Assert.assertEquals(expected, actual);

        verify(ticketDAO, times(1)).getAllForManager(user.getId(), "default", "");
        verify(userDAO, times(1)).getByLogin(user.getEmail());
    }

    @Test
    public void saveNewTicketTest() {
        final User user = new User();
        user.setRole(Role.ROLE_EMPLOYEE);
        user.setEmail("employee1_mogilev@yopmail.com");
        user.setPassword("P@ssword1");

        when(userDAO.getByLogin(user.getEmail())).thenReturn(user);

        final TicketCreationOrEditDto ticketOne = new TicketCreationOrEditDto();

        ticketOne.setName("new task");
        ticketOne.setDesiredResolutionDate(LocalDate.parse("2023-04-15"));
        ticketOne.setUrgency(Urgency.AVERAGE);
        ticketOne.setDescription("ooo");

        final Ticket ticket = new Ticket();

        ticket.setName(ticketOne.getName());
        ticket.setDesiredResolutionDate(ticketOne.getDesiredResolutionDate());
        ticket.setUrgency(ticketOne.getUrgency());

        when(ticketConverter.fromTicketCreationOrEditDto(ticketOne)).thenReturn(ticket);

        Ticket result = ticketService.save(ticketOne, user.getEmail(), "SaveAsDraft");

        Assert.assertNotNull(result);
        Assert.assertEquals(ticket.getName(), result.getName());

        verify(ticketDAO, times(1)).save(ticketConverter.fromTicketCreationOrEditDto(ticketOne));
        verify(userDAO, times(1)).getByLogin(user.getEmail());
    }

    @Test
    public void getTicketById_ThenReturnTicket() {
        Ticket ticket = new Ticket();
        Long id = 6L;
        ticket.setName("task 6");
        ticket.setDesiredResolutionDate(LocalDate.parse("2023-03-12"));
        ticket.setState(State.APPROVED);
        ticket.setUrgency(Urgency.CRITICAL);

        when(ticketDAO.getById(id)).thenReturn(ticket);

        TicketOverviewDto expected = ticketConverter.convertToTicketOverviewDto(ticket);

        TicketOverviewDto actual = ticketService.getById(id);

        Assert.assertEquals(expected, actual);

        verify(ticketDAO, times(1)).getById(id);
    }

    @Test(expected = TicketNotFoundException.class)
    public void getTicketById_IfTicketNotFound_ThenReturnException() {
        when(ticketDAO.getById(7L)).thenThrow(TicketNotFoundException.class);

        ticketService.getById(7L);

        verify(ticketDAO, times(1)).getById(7L);
    }

    @Test
    public void updateTicketTest() {
        Long id = 6L;

        final User user = new User();
        user.setRole(Role.ROLE_EMPLOYEE);
        user.setEmail("employee1_mogilev@yopmail.com");
        user.setPassword("P@ssword1");

        final Ticket ticket = new Ticket();
        ticket.setName("task 1");
        ticket.setDesiredResolutionDate(LocalDate.parse("2022-03-12"));
        ticket.setState(State.DRAFT);
        ticket.setUrgency(Urgency.CRITICAL);

        when(userDAO.getByLogin(user.getEmail())).thenReturn(user);

        final TicketCreationOrEditDto ticketCreationOrEditDto = new TicketCreationOrEditDto();

        when(ticketConverter.fromTicketCreationOrEditDto(ticketCreationOrEditDto)).thenReturn(ticket);

        Ticket result = ticketService.update(id, ticketCreationOrEditDto, user.getEmail(), "SaveAsDraft");

        Assert.assertNotNull(result);
        Assert.assertEquals(ticket, result);

        verify(ticketDAO, times(1)).update(ticketConverter.fromTicketCreationOrEditDto(ticketCreationOrEditDto));
        verify(userDAO, times(1)).getByLogin(user.getEmail());
    }
}
