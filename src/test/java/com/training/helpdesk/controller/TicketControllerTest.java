package com.training.helpdesk.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.helpdesk.dto.ticket.TicketCreationOrEditDto;
import com.training.helpdesk.dto.ticket.TicketForListDto;
import com.training.helpdesk.mail.service.EmailService;
import com.training.helpdesk.model.Category;
import com.training.helpdesk.model.Ticket;
import com.training.helpdesk.model.enums.State;
import com.training.helpdesk.model.enums.Urgency;
import com.training.helpdesk.service.TicketService;
import com.training.helpdesk.service.ValidationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TicketControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String[] CLEAN_TABLES_SQL = {
            "delete from comment",
            "delete from history",
            "delete from ticket"
    };

    @AfterEach
    public void resetDb() {
        for (String query : CLEAN_TABLES_SQL) {
            jdbcTemplate.execute(query);
        }
    }

    @Test
    @WithMockUser(username = "user1_mogilev@yopmail.com", password = "P@ssword1")
    void givenTicket_whenCreateValidTicket_thenStatus201andTicketReturned() throws Exception {

        TicketCreationOrEditDto newTicket = new TicketCreationOrEditDto();

        newTicket.setName("monica");
        newTicket.setDescription("uy");
        newTicket.setDesiredResolutionDate(LocalDate.parse("2023-04-27"));
        newTicket.setCategory(new Category("APPLICATION_AND_SERVICES"));
        newTicket.setUrgency(Urgency.LOW);

        mockMvc.perform(
                        post("http://localhost:8081/tickets")
                                .content(objectMapper.writeValueAsString(newTicket))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("monica"))
                .andExpect(jsonPath("$.description").value("uy"))
                .andExpect(jsonPath("$.desiredResolutionDate").value("2023-04-27"))
                .andExpect(jsonPath("$.category.name").value("APPLICATION_AND_SERVICES"))
                .andExpect(jsonPath("$.urgency").value("LOW"));
    }

    @Test
    @WithMockUser(username = "user1_mogilev@yopmail.com", password = "P@ssword1")
    void givenError_whenCreateInvalidTicket_thenStatus400BadRequest() throws Exception {

        TicketCreationOrEditDto newTicket = new TicketCreationOrEditDto();

        newTicket.setName("");
        newTicket.setDescription("Привет");
        newTicket.setDesiredResolutionDate(LocalDate.parse("2022-02-27"));
        newTicket.setCategory(new Category("APPLICATION_AND_SERVICES"));
        newTicket.setUrgency(Urgency.LOW);

        mockMvc.perform(
                        post("http://localhost:8081/tickets")
                                .content(objectMapper.writeValueAsString(newTicket))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "engineer1_mogilev@yopmail.com", password = "P@ssword1")
    void givenError_whenEngineerDoesNotHaveAccessToCreateTicket_thenStatus403Forbidden() throws Exception {

        TicketCreationOrEditDto newTicket = new TicketCreationOrEditDto();

        newTicket.setName("ticket");
        newTicket.setDescription("ooooo");
        newTicket.setDesiredResolutionDate(LocalDate.now());
        newTicket.setCategory(new Category("APPLICATION_AND_SERVICES"));
        newTicket.setUrgency(Urgency.LOW);

        String errorAccessCreateTicketMessage = validationService.checkAccessToCreateTicket("engineer1_mogilev@yopmail.com");

        mockMvc.perform(
                        post("http://localhost:8081/tickets")
                                .content(objectMapper.writeValueAsString(newTicket))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden())
                .andExpect(result -> assertEquals("You can't have access to create ticket",
                        errorAccessCreateTicketMessage));
    }

    @Test
    @WithMockUser(username = "manager1_mogilev@yopmail.com", password = "P@ssword1")
    void givenId_whenGetExistingTicket_thenStatus200andTicketReturned() throws Exception {
        long id = createTestTicket("ticket", "yes", LocalDate.parse("2023-04-27"),
                new Category("APPLICATION_AND_SERVICES"), Urgency.AVERAGE,
                "manager1_mogilev@yopmail.com", "SaveAsDraft").getId();

        mockMvc.perform(
                        get("http://localhost:8081/tickets/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("ticket"))
                .andExpect(jsonPath("$.createdOn").value(String.valueOf(LocalDate.now())))
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andExpect(jsonPath("$.urgency").value("AVERAGE"))
                .andExpect(jsonPath("$.desiredResolutionDate").value("2023-04-27"))
                .andExpect(jsonPath("$.ticketOwner").value("manager1_mogilev@yopmail.com"))
                .andExpect(jsonPath("$.description").value("yes"))
                .andExpect(jsonPath("$.category").value("APPLICATION_AND_SERVICES"));
    }

    @Test
    @WithMockUser(username = "manager1_mogilev@yopmail.com", password = "P@ssword1")
    void givenError_whenGetNotExistingTicket_thenStatus404NotFound() throws Exception {
        long id = ticketService.getTotalAmount().size();

        String error = "Ticket with id " + (id + 1) + " not found";

        mockMvc.perform(
                        get("http://localhost:8081/tickets/{id}", id + 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.info").value(error));
    }

    @Test
    @WithMockUser(username = "manager2_mogilev@yopmail.com", password = "P@ssword1")
    void givenTicket_whenUpdate_thenStatus200andUpdatedTicketReturned() throws Exception {
        long id = createTestTicket("ticket", "yes", LocalDate.parse("2023-04-27"),
                new Category("APPLICATION_AND_SERVICES"), Urgency.AVERAGE,
                "manager2_mogilev@yopmail.com", "SaveAsDraft").getId();

        TicketCreationOrEditDto updatedTicket = new TicketCreationOrEditDto();

        updatedTicket.setName("monica");
        updatedTicket.setDescription("uy");
        updatedTicket.setDesiredResolutionDate(LocalDate.parse("2023-05-27"));
        updatedTicket.setCategory(new Category("APPLICATION_AND_SERVICES"));
        updatedTicket.setUrgency(Urgency.LOW);

        mockMvc.perform(
                        put("http://localhost:8081/tickets/{id}", id)
                                .content(objectMapper.writeValueAsString(updatedTicket))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("monica"))
                .andExpect(jsonPath("$.description").value("uy"))
                .andExpect(jsonPath("$.desiredResolutionDate").value("2023-05-27"))
                .andExpect(jsonPath("$.category.name").value("APPLICATION_AND_SERVICES"))
                .andExpect(jsonPath("$.urgency").value("LOW"));
    }

    @Test
    @WithMockUser(username = "manager2_mogilev@yopmail.com", password = "P@ssword1")
    void givenError_whenUpdateNotDraftTicket_thenStatus403Forbidden() throws Exception {
        long id = createTestTicket("ticket", "yes", LocalDate.parse("2023-04-27"),
                new Category("APPLICATION_AND_SERVICES"), Urgency.AVERAGE,
                "manager2_mogilev@yopmail.com", "").getId();

        TicketCreationOrEditDto updatedTicket = new TicketCreationOrEditDto();

        updatedTicket.setName("monica");
        updatedTicket.setDescription("uy");
        updatedTicket.setDesiredResolutionDate(LocalDate.parse("2023-04-27"));
        updatedTicket.setCategory(new Category("APPLICATION_AND_SERVICES"));
        updatedTicket.setUrgency(Urgency.LOW);

        mockMvc.perform(
                        put("http://localhost:8081/tickets/{id}", id)
                                .content(objectMapper.writeValueAsString(updatedTicket))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.info").value("You can't have access to update ticket"));
    }

    @Test
    @WithMockUser(username = "engineer2_mogilev@yopmail.com", password = "P@ssword1")
    void givenError_whenEngineerUpdateTicket_thenStatus403Forbidden() throws Exception {
        long id = createTestTicket("ticket", "yes", LocalDate.parse("2023-04-27"),
                new Category("APPLICATION_AND_SERVICES"), Urgency.AVERAGE,
                "manager2_mogilev@yopmail.com", "SaveAsDraft").getId();

        TicketCreationOrEditDto updatedTicket = new TicketCreationOrEditDto();

        updatedTicket.setName("monica");
        updatedTicket.setDescription("uy");
        updatedTicket.setDesiredResolutionDate(LocalDate.parse("2023-04-27"));
        updatedTicket.setCategory(new Category("APPLICATION_AND_SERVICES"));
        updatedTicket.setUrgency(Urgency.LOW);

        mockMvc.perform(
                        put("http://localhost:8081/tickets/{id}", id)
                                .content(objectMapper.writeValueAsString(updatedTicket))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.info").value("You can't have access to update ticket"));
    }

    @Test
    @WithMockUser(username = "manager1_mogilev@yopmail.com", password = "P@ssword1")
    void givenAllTickets_whenIamAManager_thenStatus200() throws Exception {
        List<TicketForListDto> tickets = ticketService.getAll("manager1_mogilev@yopmail.com", "default",
                "", "default", "", 25, 1);

        mockMvc.perform(
                        get("http://localhost:8081/tickets/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tickets)));
    }

    @Test
    @WithMockUser(username = "manager1_mogilev@yopmail.com", password = "P@ssword1")
    void givenError_whenISearchWrongParameters_thenStatus400BadRequest() throws Exception {
        String wrongSearchParameterError = validationService.getWrongSearchParameterError("тикет");

        mockMvc.perform(
                        get("http://localhost:8081/tickets/all?searchField=name&parameter=тикет"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("Wrong search parameter",
                        wrongSearchParameterError));
    }

    @Test
    @WithMockUser(username = "manager2_mogilev@yopmail.com", password = "P@ssword1")
    void givenMyTickets_whenIamAManagerAndHaveSearchAndSortParameters_thenStatus200() throws Exception {
        List<TicketForListDto> tickets = ticketService.getMy("manager2_mogilev@yopmail.com", "name",
                "t", "id", "desc", 10, 1);

        mockMvc.perform(
                        get("http://localhost:8081/tickets/my?pageSize=10&pageNumber=1&sortField=id&sortDirection=desc&searchField=name&parameter=t"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tickets)));
    }

    @Test
    @WithMockUser(username = "manager1_mogilev@yopmail.com", password = "P@ssword1")
    void givenTicket_whenChangeStatusFromNewToApproved_thenStatus201() throws Exception {
        long id = createTestTicket("ticket", "yes", LocalDate.parse("2023-04-27"),
                new Category("BENEFITS_AND_PAPER_WORK"), Urgency.CRITICAL,
                "user1_mogilev@yopmail.com", "").getId();

        ticketService.changeState("user1_mogilev@yopmail.com", id, State.valueOf("approved".toUpperCase()));

        mockMvc.perform(
                        put("http://localhost:8081/tickets/{id}/change-status?newStatus=approved", id))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "manager1_mogilev@yopmail.com", password = "P@ssword1")
    void givenError_whenNoAccessToChangeStatusFromNewToDoneForManager_thenStatus403() throws Exception {
        long id = createTestTicket("ticket", "yes", LocalDate.parse("2023-04-27"),
                new Category("BENEFITS_AND_PAPER_WORK"), Urgency.CRITICAL,
                "user1_mogilev@yopmail.com", "").getId();

        ticketService.changeState("user1_mogilev@yopmail.com", id, State.valueOf("done".toUpperCase()));

        mockMvc.perform(
                        put("http://localhost:8081/tickets/{id}/change-status?newStatus=done", id))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.info").value("You can't change state current ticket"));
    }

    @Test
    @WithMockUser(username = "manager1_mogilev@yopmail.com", password = "P@ssword1")
    void givenError_whenNoAccessToChangeStatusIfOwnTicket_thenStatus403() throws Exception {
        long id = createTestTicket("ticket", "yes", LocalDate.parse("2023-04-27"),
                new Category("BENEFITS_AND_PAPER_WORK"), Urgency.CRITICAL,
                "manager1_mogilev@yopmail.com", "").getId();

        ticketService.changeState("manager1_mogilev@yopmail.com", id, State.valueOf("approved".toUpperCase()));

        mockMvc.perform(
                        put("http://localhost:8081/tickets/{id}/change-status?newStatus=approved", id))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.info").value("You can't formatted your own ticket"));
    }

    private Ticket createTestTicket(String name, String description, LocalDate desiredResolutionDate,
                                    Category category, Urgency urgency, String login, String buttonValue) {
        TicketCreationOrEditDto ticket = new TicketCreationOrEditDto();

        ticket.setName(name);
        ticket.setDescription(description);
        ticket.setDesiredResolutionDate(desiredResolutionDate);
        ticket.setCategory(category);
        ticket.setUrgency(urgency);

        return ticketService.save(ticket, login, buttonValue);
    }
}