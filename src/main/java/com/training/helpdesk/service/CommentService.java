package com.training.helpdesk.service;

import com.training.helpdesk.dto.comment.CommentDto;
import com.training.helpdesk.model.Comment;

import java.util.List;

public interface CommentService {

    Comment save(CommentDto commentDto, Long ticketId);

    List<CommentDto> getAllByTicketId(Long ticketId, String buttonValue);
}
