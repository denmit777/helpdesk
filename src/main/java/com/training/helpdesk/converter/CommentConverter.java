package com.training.helpdesk.converter;

import com.training.helpdesk.dto.comment.CommentDto;
import com.training.helpdesk.model.Comment;

public interface CommentConverter {

    CommentDto convertToCommentDto(Comment comment);

    Comment fromCommentDto(CommentDto commentDto, Long ticketId);
}
