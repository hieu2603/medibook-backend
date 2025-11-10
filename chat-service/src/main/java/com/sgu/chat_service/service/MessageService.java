package com.sgu.chat_service.service;

import com.sgu.chat_service.dto.response.MessageResponseDto;
import com.sgu.common.dto.PaginationResponse;

import java.util.UUID;

public interface MessageService {
    PaginationResponse<MessageResponseDto> getMessages(
            UUID conversationId,
            int page, int size,
            UUID currentUserId
    );

    MessageResponseDto sendMessage(UUID senderId, UUID receiverId, String content);
}
