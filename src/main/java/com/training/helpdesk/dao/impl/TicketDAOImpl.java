package com.training.helpdesk.dao.impl;

import com.training.helpdesk.dao.TicketDAO;
import com.training.helpdesk.exception.TicketNotFoundException;
import com.training.helpdesk.model.Ticket;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class TicketDAOImpl implements TicketDAO {

    private static final Map<String, List<Ticket>> SEARCH_MAP = new HashMap<>();
    private static final Set<Map.Entry<String, List<Ticket>>> SEARCH_SET = SEARCH_MAP.entrySet();

    private static final String QUERY_SELECT_FROM_TICKET = "from Ticket t";

    private static final String QUERY_SELECT_FROM_TICKET_FOR_EMPLOYEE_CREATED_BY_HIM = "from Ticket t where t.ticketOwner.id =:employeeId and t.ticketOwner.role like 'ROLE_EMPLOYEE'";

    private static final String QUERY_SELECT_FROM_TICKET_FOR_MANAGER_CREATED_BY_HIM = "from Ticket t where t.ticketOwner.id =:managerId and t.ticketOwner.role like 'ROLE_MANAGER'";
    private static final String QUERY_SELECT_FROM_TICKET_FOR_MANAGER_CREATED_BY_ALL_EMPLOYEES = "from Ticket t where t.ticketOwner.role like 'ROLE_EMPLOYEE' and t.state like 'NEW'";
    private static final String QUERY_SELECT_FROM_TICKET_FOR_MANAGER_APPROVED_BY_HIM = "from Ticket t where t.approver.id =:managerId and t.state in ('APPROVED', 'DECLINED', 'CANCELED', 'IN_PROGRESS', 'DONE')";
    private static final String QUERY_SELECT_FROM_TICKET_FOR_MANAGER_APPROVED_BY_HIM_IN_APPROVED = "from Ticket t where t.approver.id =:managerId and t.state like 'APPROVED'";

    private static final String QUERY_SELECT_FROM_TICKET_FOR_ENGINEER_CREATED_BY_ALL = "from Ticket t where t.ticketOwner.role in ('ROLE_EMPLOYEE', 'ROLE_MANAGER') and t.state like 'APPROVED'";
    private static final String QUERY_SELECT_FROM_TICKET_FOR_ENGINEER_ASSIGNED_TO_HIM = "from Ticket t where t.assignee.id =:engineerId and t.state in ('IN_PROGRESS', 'DONE')";
    private static final String QUERY_SELECT_FROM_TICKET_FOR_ENGINEER_ASSIGNED_TO_HIM_IN_ALL_STATUSES = "from Ticket t where t.assignee.id =:engineerId";

    private static final String QUERY_SELECT_FROM_TICKET_SEARCHED_BY_ID = " and str(t.id) like concat('%',?0,'%')";
    private static final String QUERY_SELECT_FROM_TICKET_SEARCHED_BY_NAME = " and lower(t.name) like lower(concat('%',?0,'%'))";
    private static final String QUERY_SELECT_FROM_TICKET_SEARCHED_BY_DESIRED_DATE = " and str(t.desiredResolutionDate) like concat('%',?0,'%')";
    private static final String QUERY_SELECT_FROM_TICKET_SEARCHED_BY_URGENCY = " and lower(str(t.urgency)) like lower(concat('%',?0,'%'))";
    private static final String QUERY_SELECT_FROM_TICKET_SEARCHED_BY_STATUS = " and lower(str(t.state)) like lower(concat('%',?0,'%'))";
    private static final String QUERY_SELECT_DRAFTS_FROM_TICKET = "from Ticket t where t.id = :id and t.ticketOwner.id = :userId and t.state = 'DRAFT' and t.ticketOwner.role in ('ROLE_EMPLOYEE', 'ROLE_MANAGER')";
    private static final String QUERY_SELECT_FROM_DONE_TICKET = "from Ticket t where t.id = :id and t.ticketOwner.id = :userId and t.state = 'DONE' and t.ticketOwner.role in ('ROLE_EMPLOYEE', 'ROLE_MANAGER')";

    @PersistenceContext
    private final EntityManager entityManager;

    public TicketDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Ticket ticket) {
        entityManager.persist(ticket);
    }

    @Override
    public Ticket getById(Long id) {
        return getAll().stream()
                .filter(ticket -> id.equals(ticket.getId()))
                .findAny()
                .orElseThrow(() -> new TicketNotFoundException(String.format("Ticket with id %s not found", id)));
    }

    @Override
    public void update(Ticket ticket) {
        entityManager.merge(ticket);
    }

    @Override
    public List<Ticket> getAllForEmployee(Long employeeId, String searchField, String parameter) {
        return getAllSearchedByParameters(QUERY_SELECT_FROM_TICKET_FOR_EMPLOYEE_CREATED_BY_HIM,
                "employeeId", employeeId, searchField, parameter);
    }

    @Override
    public List<Ticket> getAllForManager(Long managerId, String searchField, String parameter) {
        List<Ticket> ticketsCreatedByAllEmployees =
                getAllSearchedByParameters(QUERY_SELECT_FROM_TICKET_FOR_MANAGER_CREATED_BY_ALL_EMPLOYEES,
                        "", 0L, searchField, parameter);

        List<Ticket> ticketsCreatedByManager = getAllSearchedByParameters(QUERY_SELECT_FROM_TICKET_FOR_MANAGER_CREATED_BY_HIM,
                "managerId", managerId, searchField, parameter);

        List<Ticket> ticketsApprovedByManager = getAllSearchedByParameters(QUERY_SELECT_FROM_TICKET_FOR_MANAGER_APPROVED_BY_HIM,
                "managerId", managerId, searchField, parameter);

        return Stream.of(ticketsCreatedByAllEmployees, ticketsCreatedByManager, ticketsApprovedByManager)
                .flatMap(List<Ticket>::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ticket> getMyForManager(Long managerId, String searchField, String parameter) {
        List<Ticket> ticketsCreatedByManager = getAllSearchedByParameters(QUERY_SELECT_FROM_TICKET_FOR_MANAGER_CREATED_BY_HIM,
                "managerId", managerId, searchField, parameter);

        List<Ticket> ticketsApprovedByManagerInApproved = getAllSearchedByParameters(QUERY_SELECT_FROM_TICKET_FOR_MANAGER_APPROVED_BY_HIM_IN_APPROVED,
                "managerId", managerId, searchField, parameter);

        return Stream.of(ticketsCreatedByManager, ticketsApprovedByManagerInApproved)
                .flatMap(List<Ticket>::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ticket> getAllForEngineer(Long engineerId, String searchField, String parameter) {
        List<Ticket> ticketsCreatedByAllEmployeesAndManagers =
                getAllSearchedByParameters(QUERY_SELECT_FROM_TICKET_FOR_ENGINEER_CREATED_BY_ALL,
                        "", 0L, searchField, parameter);

        List<Ticket> ticketsAssignedToEngineer = getAllSearchedByParameters(QUERY_SELECT_FROM_TICKET_FOR_ENGINEER_ASSIGNED_TO_HIM,
                "engineerId", engineerId, searchField, parameter);

        return Stream.of(ticketsCreatedByAllEmployeesAndManagers, ticketsAssignedToEngineer)
                .flatMap(List<Ticket>::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ticket> getMyForEngineer(Long engineerId, String searchField, String parameter) {
        return getAllSearchedByParameters(QUERY_SELECT_FROM_TICKET_FOR_ENGINEER_ASSIGNED_TO_HIM_IN_ALL_STATUSES,
                "engineerId", engineerId, searchField, parameter);
    }

    @Override
    public List<Ticket> getAll() {
        return entityManager.createQuery(QUERY_SELECT_FROM_TICKET)
                .getResultList();
    }

    @Override
    public Optional<Ticket> checkAccessToDraftTicket(Long userId, Long ticketId) {
        return entityManager.createQuery(QUERY_SELECT_DRAFTS_FROM_TICKET, Ticket.class)
                .setParameter("id", ticketId)
                .setParameter("userId", userId)
                .getResultStream()
                .findAny();
    }

    @Override
    public Optional<Ticket> checkAccessToFeedbackTicket(Long userId, Long ticketId) {
        return entityManager.createQuery(QUERY_SELECT_FROM_DONE_TICKET, Ticket.class)
                .setParameter("id", ticketId)
                .setParameter("userId", userId)
                .getResultStream()
                .findAny();
    }

    private List<Ticket> createQueryWithPageSizeAndPageNumber(String mainQuery, String paramName, Long paramValue,
                                                              String searchQuery, String searchParameter) {
        Query query = entityManager.createQuery(mainQuery + searchQuery);

        if (paramValue != 0L) {
            query.setParameter(paramName, paramValue);
        }
        if (!(searchQuery.equals(""))) {
            query.setParameter(0, searchParameter);
        }

        return query.getResultList();
    }

    private List<Ticket> getAllSearchedByParameters(String mainQuery, String paramName, Long paramValue,
                                                    String searchField, String searchParameter) {

        SEARCH_MAP.put("id", createQueryWithPageSizeAndPageNumber(mainQuery, paramName, paramValue,
                QUERY_SELECT_FROM_TICKET_SEARCHED_BY_ID, searchParameter));
        SEARCH_MAP.put("name", createQueryWithPageSizeAndPageNumber(mainQuery, paramName, paramValue,
                QUERY_SELECT_FROM_TICKET_SEARCHED_BY_NAME, searchParameter));
        SEARCH_MAP.put("desiredResolutionDate", createQueryWithPageSizeAndPageNumber(mainQuery, paramName, paramValue,
                QUERY_SELECT_FROM_TICKET_SEARCHED_BY_DESIRED_DATE, searchParameter));
        SEARCH_MAP.put("urgency", createQueryWithPageSizeAndPageNumber(mainQuery, paramName, paramValue,
                QUERY_SELECT_FROM_TICKET_SEARCHED_BY_URGENCY, searchParameter));
        SEARCH_MAP.put("status", createQueryWithPageSizeAndPageNumber(mainQuery, paramName, paramValue,
                QUERY_SELECT_FROM_TICKET_SEARCHED_BY_STATUS, searchParameter));
        SEARCH_MAP.put("default", createQueryWithPageSizeAndPageNumber(mainQuery, paramName, paramValue,
                "", ""));

        return SEARCH_SET.stream()
                .filter(pair -> pair.getKey().equals(searchField))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
