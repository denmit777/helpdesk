package com.training.helpdesk.dao.impl;

import com.training.helpdesk.dao.AttachmentDAO;
import com.training.helpdesk.model.Attachment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AttachmentDAOImpl implements AttachmentDAO {

    private static final String QUERY_SELECT_FROM_ATTACHMENT_BY_TICKED_ID = "from Attachment a where a.ticket.id =:id";
    private static final String QUERY_SELECT_FROM_ATTACHMENT_BY_TICKED_ID_AND_ATTACHMENT_ID = "from Attachment a where a.ticket.id =:ticketId and a.id =:attachmentId";
    private static final String QUERY_DELETE_FROM_ATTACHMENT_BY_TICKED_ID_AND_ATTACHMENT_ID = "delete from Attachment a where a.ticket.id =:ticketId and a.id =:attachmentId";

    @PersistenceContext
    private final EntityManager entityManager;

    public AttachmentDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Attachment attachment) {
        entityManager.persist(attachment);
    }

    @Override
    public Attachment getByAttachmentIdAndTicketId(Long attachmentId, Long ticketId) {
        return (Attachment) entityManager.createQuery(QUERY_SELECT_FROM_ATTACHMENT_BY_TICKED_ID_AND_ATTACHMENT_ID)
                .setParameter("attachmentId", attachmentId)
                .setParameter("ticketId", ticketId)
                .getSingleResult();
    }

    @Override
    public List<Attachment> getAllByTicketId(Long id) {
        return entityManager.createQuery(QUERY_SELECT_FROM_ATTACHMENT_BY_TICKED_ID)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public void deleteByAttachmentIdAndTicketId(Long attachmentId, Long ticketId) {
        entityManager.createQuery(QUERY_DELETE_FROM_ATTACHMENT_BY_TICKED_ID_AND_ATTACHMENT_ID)
                .setParameter("attachmentId", attachmentId)
                .setParameter("ticketId", ticketId)
                .executeUpdate();
    }
}
