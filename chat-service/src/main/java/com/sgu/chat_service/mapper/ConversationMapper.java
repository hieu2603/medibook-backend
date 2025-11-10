package com.sgu.chat_service.mapper;

import com.sgu.chat_service.dto.response.ConversationResponseDto;
import com.sgu.chat_service.model.Conversation;

public class ConversationMapper {
    // Từ Entity -> Response DTO
    public static ConversationResponseDto toDto(Conversation conversation) {
        return ConversationResponseDto.builder()
                .id(conversation.getId())
                .participant1(conversation.getParticipant1())
                .participant2(conversation.getParticipant2())
                .createdAt(conversation.getCreatedAt())
                .build();
    }
}
