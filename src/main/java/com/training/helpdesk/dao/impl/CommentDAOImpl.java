package com.training.helpdesk.dao.impl;

import com.training.helpdesk.dao.CommentDAO;
import com.training.helpdesk.model.Comment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CommentDAOImpl implements CommentDAO {

    private static final String QUERY_SELECT_FROM_COMMENT_BY_TICKED_ID = "from Comment c where c.ticket.id =:id";
    private static final String QUERY_SELECT_FROM_COMMENT_BY_TICKED_ID_ORDER_BY_DATE = "from Comment c where c.ticket.id =:id order by date DESC";

    @PersistenceContext
    private final EntityManager entityManager;

    public CommentDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Comment comment) {
        entityManager.persist(comment);
    }

    @Override
    public List<Comment> getLastFiveByTicketId(Long id) {
        return entityManager.createQuery(QUERY_SELECT_FROM_COMMENT_BY_TICKED_ID_ORDER_BY_DATE)
                .setParameter("id", id)
                .setMaxResults(5)
                .getResultList();
    }

    @Override
    public List<Comment> getAllByTicketId(Long id) {
        return entityManager.createQuery(QUERY_SELECT_FROM_COMMENT_BY_TICKED_ID)
                .setParameter("id", id)
                .getResultList();
    }
}
