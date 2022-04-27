package com.training.helpdesk.dao.impl;

import com.training.helpdesk.dao.HistoryDAO;
import com.training.helpdesk.model.History;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class HistoryDAOImpl implements HistoryDAO {

    private static final String QUERY_SELECT_FROM_HISTORY_BY_TICKED_ID = "from History h where h.ticket.id =:id";
    private static final String QUERY_SELECT_FROM_HISTORY_BY_TICKED_ID_ORDER_BY_DATE = "from History h where h.ticket.id =:id order by date DESC";

    @PersistenceContext
    private final EntityManager entityManager;

    public HistoryDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(History history) {
        entityManager.persist(history);
    }

    @Override
    public List<History> getLastFiveByTicketId(Long id) {
        return entityManager.createQuery(QUERY_SELECT_FROM_HISTORY_BY_TICKED_ID_ORDER_BY_DATE)
                .setParameter("id", id)
                .setMaxResults(5)
                .getResultList();
    }

    @Override
    public List<History> getAllByTicketId(Long id) {
        return entityManager.createQuery(QUERY_SELECT_FROM_HISTORY_BY_TICKED_ID)
                .setParameter("id", id)
                .getResultList();
    }
}
