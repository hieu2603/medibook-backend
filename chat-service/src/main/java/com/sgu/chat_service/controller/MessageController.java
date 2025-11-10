package com.sgu.chat_service.controller;

import com.sgu.chat_service.dto.request.MessageRequestDto;
import com.sgu.chat_service.dto.response.MessageResponseDto;
import com.sgu.chat_service.service.MessageService;
import com.sgu.common.dto.ApiResponse;
import com.sgu.common.dto.PaginationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/{conversationId}")
    public ResponseEntity<ApiResponse<List<MessageResponseDto>>> getMessages(
            @PathVariable UUID conversationId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader("X-User-Id") String id
    ) {
        PaginationResponse<MessageResponseDto> result = messageService
                .getMessages(conversationId, page, size, UUID.fromString(id));

        ApiResponse<List<MessageResponseDto>> response = ApiResponse.<List<MessageResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .message("Messages retrieved successfully")
                .data(result.getData())
                .meta(result.getMeta())
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Valid @Payload MessageRequestDto dto) {
        MessageResponseDto newMessage = messageService.sendMessage(
                dto.getSenderId(),
                dto.getReceiverId(),
                dto.getContent()
        );

        messagingTemplate.convertAndSend(
                "/topic/conversation." + newMessage.getConversationId(),
                newMessage
        );
    }

    // Test
    @PostMapping("/send")
    public ResponseEntity<ApiResponse<MessageResponseDto>> sendMessageRest(
            @Valid @RequestBody MessageRequestDto dto
    ) {
        MessageResponseDto message = messageService.sendMessage(
                dto.getSenderId(), dto.getReceiverId(), dto.getContent()
        );

        ApiResponse<MessageResponseDto> response = ApiResponse.<MessageResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("Sent!")
                .data(message)
                .build();

        return ResponseEntity.ok(response);
    }
}
