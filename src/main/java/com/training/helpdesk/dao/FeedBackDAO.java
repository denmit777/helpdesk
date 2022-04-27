package com.training.helpdesk.dao;

import com.training.helpdesk.model.Feedback;

import java.util.List;

public interface FeedBackDAO {

    void save(Feedback feedBack);

    List<Feedback> getLastFiveByTicketId(Long id);

    List<Feedback> getAllByTicketId(Long id);
}
