package com.sgu.chat_service.mapper;

import com.sgu.chat_service.dto.response.MessageResponseDto;
import com.sgu.chat_service.model.Message;

public class MessageMapper {

    // Từ Entity -> Response DTO
    public static MessageResponseDto toDto(Message message) {
        return MessageResponseDto.builder()
                .id(message.getId())
                .conversationId(message.getConversationId())
                .senderId(message.getSenderId())
                .receiverId(message.getReceiverId())
                .content(message.getContent())
                .sentAt(message.getSentAt())
                .build();
    }
}
