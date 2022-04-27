package com.training.helpdesk.service;

import com.training.helpdesk.dto.history.HistoryDto;
import com.training.helpdesk.model.History;

import java.util.List;

public interface HistoryService {

    History save(HistoryDto historyDto, Long ticketId, String previousStatus, String newStatus);

    List<HistoryDto> getAllByTicketId(Long ticketId, String buttonValue);
}
