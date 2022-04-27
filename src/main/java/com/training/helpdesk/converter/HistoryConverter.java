package com.training.helpdesk.converter;

import com.training.helpdesk.dto.history.HistoryDto;
import com.training.helpdesk.model.History;

public interface HistoryConverter {

    HistoryDto convertToHistoryDto(History history);

    History fromHistoryDto(HistoryDto historyDto, Long ticketId);
}
