package com.sgu.chat_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ConversationResponseDto {
    private UUID id;
    private UUID participant1;
    private UUID participant2;
    private LocalDateTime createdAt;
}
