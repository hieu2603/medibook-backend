package com.sgu.chat_service.service;

import com.sgu.chat_service.dto.response.ConversationResponseDto;

import java.util.UUID;

public interface ConversationService {
    ConversationResponseDto getOrCreateConversation(UUID user1, UUID user2);
}
