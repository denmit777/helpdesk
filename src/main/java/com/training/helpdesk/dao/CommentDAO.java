package com.training.helpdesk.dao;

import com.training.helpdesk.model.Comment;

import java.util.List;

public interface CommentDAO {

    void save(Comment comment);

    List<Comment> getLastFiveByTicketId(Long ticketId);

    List<Comment> getAllByTicketId(Long ticketId);
}
