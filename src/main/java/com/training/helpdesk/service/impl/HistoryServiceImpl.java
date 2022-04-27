package com.training.helpdesk.service.impl;

import com.training.helpdesk.converter.HistoryConverter;
import com.training.helpdesk.dao.HistoryDAO;
import com.training.helpdesk.dto.history.HistoryDto;
import com.training.helpdesk.model.History;
import com.training.helpdesk.service.HistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryServiceImpl implements HistoryService {

    private static final String ACTION_CHANGE_TICKET_STATUS = "Ticket Status is changed";

    private final HistoryDAO historyDAO;
    private final HistoryConverter historyConverter;

    public HistoryServiceImpl(HistoryDAO historyDAO, HistoryConverter historyConverter) {
        this.historyDAO = historyDAO;
        this.historyConverter = historyConverter;
    }

    @Override
    @Transactional
    public History save(HistoryDto historyDto, Long ticketId, String previousStatus, String newStatus) {
        History history = historyConverter.fromHistoryDto(historyDto, ticketId);

        if (!previousStatus.equals("") && !newStatus.equals("")) {
            history.setAction(ACTION_CHANGE_TICKET_STATUS);
            history.setDescription(String.format("Ticket Status is changed from %s to %s.", previousStatus, newStatus));
        }

        historyDAO.save(history);

        return history;
    }

    @Override
    @Transactional
    public List<HistoryDto> getAllByTicketId(Long ticketId, String buttonValue) {
        List<History> histories;

        if (buttonValue.equals("Show All")) {
            histories = historyDAO.getAllByTicketId(ticketId);
        } else {
            histories = historyDAO.getLastFiveByTicketId(ticketId);
        }

        return histories.stream()
                .map(historyConverter::convertToHistoryDto)
                .collect(Collectors.toList());
    }
}
