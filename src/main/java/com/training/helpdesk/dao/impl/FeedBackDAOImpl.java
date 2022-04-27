package com.training.helpdesk.dao.impl;

import com.training.helpdesk.dao.FeedBackDAO;
import com.training.helpdesk.model.Feedback;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class FeedBackDAOImpl implements FeedBackDAO {

    private static final String QUERY_SELECT_FROM_FEEDBACK_BY_TICKED_ID = "from Feedback f where f.ticket.id =:id";
    private static final String QUERY_SELECT_FROM_FEEDBACK_BY_TICKED_ID_ORDER_BY_DATE = "from Feedback f where f.ticket.id =:id order by date DESC";

    @PersistenceContext
    private final EntityManager entityManager;

    public FeedBackDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Feedback feedBack) {
        entityManager.persist(feedBack);
    }

    @Override
    public List<Feedback> getLastFiveByTicketId(Long id) {
        return entityManager.createQuery(QUERY_SELECT_FROM_FEEDBACK_BY_TICKED_ID_ORDER_BY_DATE)
                .setParameter("id", id)
                .setMaxResults(5)
                .getResultList();
    }

    @Override
    public List<Feedback> getAllByTicketId(Long id) {
        return entityManager.createQuery(QUERY_SELECT_FROM_FEEDBACK_BY_TICKED_ID)
                .setParameter("id", id)
                .getResultList();
    }
}
