package com.sgu.chat_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class MessageRequestDto {
    @NotNull(message = "Sender ID is required")
    private UUID senderId;

    @NotNull(message = "Receiver ID is required")
    private UUID receiverId;

    @NotBlank(message = "Content is required")
    @Size(max = 1000, message = "Content must be less than 1000 characters")
    private String content;
}
