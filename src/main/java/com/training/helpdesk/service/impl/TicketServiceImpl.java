package com.training.helpdesk.service.impl;

import com.training.helpdesk.converter.TicketConverter;
import com.training.helpdesk.dao.TicketDAO;
import com.training.helpdesk.dao.UserDAO;
import com.training.helpdesk.dto.ticket.TicketCreationOrEditDto;
import com.training.helpdesk.dto.ticket.TicketForListDto;
import com.training.helpdesk.dto.ticket.TicketOverviewDto;
import com.training.helpdesk.model.Ticket;
import com.training.helpdesk.model.User;
import com.training.helpdesk.model.enums.Role;
import com.training.helpdesk.model.enums.State;
import com.training.helpdesk.mail.service.EmailGenerateService;
import com.training.helpdesk.mail.service.EmailService;
import com.training.helpdesk.service.TicketService;
import com.training.helpdesk.util.comparator.StatusComparator;
import com.training.helpdesk.util.comparator.UrgencyComparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketDAO ticketDAO;
    private final TicketConverter ticketConverter;
    private final UserDAO userDAO;
    private final EmailService emailService;
    private final EmailGenerateService emailGenerateService;

    private static final Map<String, List<Ticket>> SORT_MAP = new HashMap<>();
    private static final Set<Map.Entry<String, List<Ticket>>> SORT_SET = SORT_MAP.entrySet();

    public TicketServiceImpl(TicketDAO ticketDAO, TicketConverter ticketConverter, UserDAO userDAO,
                             EmailService emailService, EmailGenerateService emailGenerateService) {
        this.ticketDAO = ticketDAO;
        this.ticketConverter = ticketConverter;
        this.userDAO = userDAO;
        this.emailService = emailService;
        this.emailGenerateService = emailGenerateService;
    }

    @Override
    @Transactional
    public Ticket save(TicketCreationOrEditDto ticketCreationDto, String login, String buttonValue) {
        Ticket ticket = ticketConverter.fromTicketCreationOrEditDto(ticketCreationDto);

        User user = userDAO.getByLogin(login);

        ticket.setTicketOwner(user);

        if (buttonValue.equals("SaveAsDraft")) {
            ticket.setState(State.DRAFT);
        } else {
            ticket.setState(State.NEW);

            sendEmailAfterSubmitNewTicket();
        }

        ticketDAO.save(ticket);

        return ticket;
    }

    @Override
    @Transactional
    public TicketOverviewDto getById(Long id) {
        return ticketConverter.convertToTicketOverviewDto(ticketDAO.getById(id));
    }

    @Override
    @Transactional
    public Ticket update(Long id, TicketCreationOrEditDto ticketEditDto, String login, String buttonValue) {
        Ticket ticket = ticketConverter.fromTicketCreationOrEditDto(ticketEditDto);

        User user = userDAO.getByLogin(login);

        ticket.setTicketOwner(user);

        ticket.setId(id);

        if (buttonValue.equals("SaveAsDraft")) {
            ticket.setState(State.DRAFT);
        } else {
            ticket.setState(State.NEW);

            emailService.sendNewTicketMail(id);
        }

        ticketDAO.update(ticket);

        return ticket;
    }

    @Override
    @Transactional
    public List<TicketForListDto> getAll(String login, String searchField, String parameter, String sortField,
                                         String sortDirection, int pageSize, int pageNumber) {
        User user = userDAO.getByLogin(login);

        List<Ticket> tickets = new ArrayList<>();

        if (user.getRole().equals(Role.ROLE_EMPLOYEE)) {
            tickets = getPage(getDirectionSort(ticketDAO.getAllForEmployee(user.getId(), searchField, parameter),
                    sortField, sortDirection), pageSize, pageNumber);
        }
        if (user.getRole().equals(Role.ROLE_MANAGER)) {
            tickets = getPage(getDirectionSort(ticketDAO.getAllForManager(user.getId(), searchField, parameter),
                    sortField, sortDirection), pageSize, pageNumber);
        }
        if (user.getRole().equals(Role.ROLE_ENGINEER)) {
            tickets = getPage(getDirectionSort(ticketDAO.getAllForEngineer(user.getId(), searchField, parameter),
                    sortField, sortDirection), pageSize, pageNumber);
        }

        return tickets.stream()
                .map(ticketConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketForListDto> getMy(String login, String searchField, String parameter, String sortField,
                                        String sortDirection, int pageSize, int pageNumber) {
        User user = userDAO.getByLogin(login);

        List<Ticket> tickets = new ArrayList<>();

        if (user.getRole().equals(Role.ROLE_EMPLOYEE)) {
            tickets = getPage(getDirectionSort(ticketDAO.getAllForEmployee(user.getId(), searchField, parameter),
                    sortField, sortDirection), pageSize, pageNumber);
        }
        if (user.getRole().equals(Role.ROLE_MANAGER)) {
            tickets = getPage(getDirectionSort(ticketDAO.getMyForManager(user.getId(), searchField, parameter),
                    sortField, sortDirection), pageSize, pageNumber);
        }
        if (user.getRole().equals(Role.ROLE_ENGINEER)) {
            tickets = getPage(getDirectionSort(ticketDAO.getMyForEngineer(user.getId(), searchField, parameter),
                    sortField, sortDirection), pageSize, pageNumber);
        }

        return tickets.stream()
                .map(ticketConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void changeState(String login, Long ticketId, State newState) {
        Ticket ticket = ticketDAO.getById(ticketId);

        User user = userDAO.getByLogin(login);

        if (newState == State.APPROVED) {
            emailGenerateService.generateEmail(ticketId, ticket.getState(), newState);

            ticket.setState(newState);
            ticket.setApprover(user);
        } else if (newState == State.IN_PROGRESS) {
            emailGenerateService.generateEmail(ticketId, ticket.getState(), newState);

            ticket.setState(newState);
            ticket.setAssignee(user);
        } else {
            emailGenerateService.generateEmail(ticketId, ticket.getState(), newState);

            ticket.setState(newState);
        }
        ticketDAO.update(ticket);
    }

    @Override
    public List<TicketForListDto> getTotalAmount() {
        List<Ticket> tickets = ticketDAO.getAll();

        return tickets.stream()
                .map(ticketConverter::convertToDto)
                .collect(Collectors.toList());
    }

    private List<Ticket> getSortedTickets(List<Ticket> tickets, String sortField) {
        SORT_MAP.put("id", tickets.stream()
                .sorted(Comparator.comparing(Ticket::getId))
                .collect(Collectors.toList()));
        SORT_MAP.put("name", tickets.stream()
                .sorted(Comparator.comparing(Ticket::getName))
                .collect(Collectors.toList()));
        SORT_MAP.put("desiredResolutionDate", tickets.stream()
                .sorted(Comparator.comparing(Ticket::getDesiredResolutionDate))
                .collect(Collectors.toList()));
        SORT_MAP.put("urgency", tickets.stream()
                .sorted(Comparator.comparing(Ticket::getUrgency, new UrgencyComparator()))
                .collect(Collectors.toList()));
        SORT_MAP.put("status", tickets.stream().
                sorted(Comparator.comparing(Ticket::getState, new StatusComparator()))
                .collect(Collectors.toList()));
        SORT_MAP.put("default", tickets.stream()
                .sorted(Comparator.comparing(Ticket::getUrgency, new UrgencyComparator())
                        .thenComparing(Comparator.comparing(Ticket::getDesiredResolutionDate).reversed()))
                .collect(Collectors.toList()));

        return SORT_SET.stream()
                .filter(pair -> pair.getKey().equals(sortField))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<Ticket> getDirectionSort(List<Ticket> tickets, String sortField, String sortDirection) {
        if (sortDirection.equals("desc")) {
            List<Ticket> reversedTickets = getSortedTickets(tickets, sortField);
            Collections.reverse(reversedTickets);

            return reversedTickets;
        }
        return getSortedTickets(tickets, sortField);
    }

    private List<Ticket> getPage(List<Ticket> tickets, int pageSize, int pageNumber) {
        List<List<Ticket>> pages = new ArrayList<>();

        for (int i = 0; i < tickets.size(); i += pageSize) {
            List<Ticket> page = new ArrayList<>(tickets.subList(i, Math.min(tickets.size(), i + pageSize)));

            pages.add(page);
        }
        if (!tickets.isEmpty() && pageNumber - 1 <= tickets.size() / pageSize) {
            return pages.get(pageNumber - 1);
        } else {
            return Collections.emptyList();
        }
    }

    private void sendEmailAfterSubmitNewTicket() {
        Long newTicketId = ticketDAO.getAll().size() + 1L;

        emailService.sendNewTicketMail(newTicketId);
    }
}

