package com.training.helpdesk.dao;

import com.training.helpdesk.model.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketDAO {

    void save(Ticket ticket);

    Ticket getById(Long id);

    void update(Ticket ticket);

    List<Ticket> getAllForEmployee(Long employeeId, String searchField, String parameter);

    List<Ticket> getAllForManager(Long managerId, String searchField, String parameter);

    List<Ticket> getMyForManager(Long managerId, String searchField, String parameter);

    List<Ticket> getAllForEngineer(Long engineerId, String searchField, String parameter);

    List<Ticket> getMyForEngineer(Long engineerId, String searchField, String parameter);

    List<Ticket> getAll();

    Optional<Ticket> checkAccessToDraftTicket(Long userId, Long ticketId);

    Optional<Ticket> checkAccessToFeedbackTicket(Long userId, Long ticketId);
}
