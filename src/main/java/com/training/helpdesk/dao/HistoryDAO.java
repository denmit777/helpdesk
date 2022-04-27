package com.training.helpdesk.dao;

import com.training.helpdesk.model.History;

import java.util.List;

public interface HistoryDAO {

    void save(History history);

    List<History> getLastFiveByTicketId(Long id);

    List<History> getAllByTicketId(Long id);
}
