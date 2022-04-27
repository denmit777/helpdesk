package com.training.helpdesk.service.impl;

import com.training.helpdesk.converter.CommentConverter;
import com.training.helpdesk.dao.CommentDAO;
import com.training.helpdesk.dto.comment.CommentDto;
import com.training.helpdesk.model.Comment;
import com.training.helpdesk.service.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentDAO commentDAO;
    private final CommentConverter commentConverter;

    public CommentServiceImpl(CommentDAO commentDAO, CommentConverter commentConverter) {
        this.commentDAO = commentDAO;
        this.commentConverter = commentConverter;
    }

    @Override
    @Transactional
    public Comment save(CommentDto commentDto, Long ticketId) {
        Comment comment = commentConverter.fromCommentDto(commentDto, ticketId);

        commentDAO.save(comment);

        return comment;
    }

    @Override
    @Transactional
    public List<CommentDto> getAllByTicketId(Long ticketId, String buttonValue) {
        List<Comment> comments;

        if (buttonValue.equals("Show All")) {
            comments = commentDAO.getAllByTicketId(ticketId);
        } else {
            comments = commentDAO.getLastFiveByTicketId(ticketId);
        }

        return comments.stream()
                .map(commentConverter::convertToCommentDto)
                .collect(Collectors.toList());
    }
}
