package com.sgu.chat_service.service.impl;

import com.sgu.chat_service.dto.response.ConversationResponseDto;
import com.sgu.chat_service.dto.response.MessageResponseDto;
import com.sgu.chat_service.mapper.MessageMapper;
import com.sgu.chat_service.model.Message;
import com.sgu.chat_service.repository.MessageRepository;
import com.sgu.chat_service.security.ChatPermissionValidator;
import com.sgu.chat_service.service.ConversationService;
import com.sgu.chat_service.service.MessageService;
import com.sgu.common.dto.PaginationMeta;
import com.sgu.common.dto.PaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final ConversationService conversationService;
    private final MessageRepository messageRepository;
    private final ChatPermissionValidator chatPermissionValidator;

    @Override
    public PaginationResponse<MessageResponseDto> getMessages(
            UUID conversationId, int page, int size, UUID currentUserId
    ) {
        chatPermissionValidator.validateGetMessages(conversationId, currentUserId);

        int pageIndex = (page <= 0) ? 0 : page - 1;

        Pageable pageable = PageRequest.of(pageIndex, size);
        Page<Message> messagePage = messageRepository
                .findByConversationIdOrderBySentAtDesc(conversationId, pageable);

        List<MessageResponseDto> data = messagePage
                .getContent()
                .stream()
                .map(MessageMapper::toDto)
                .sorted(Comparator.comparing(MessageResponseDto::getSentAt))
                .toList();

        long totalItems = messagePage.getTotalElements();

        PaginationMeta meta = PaginationMeta.builder()
                .currentPage(totalItems == 0 ? 0 : messagePage.getNumber() + 1)
                .pageSize(messagePage.getSize())
                .totalPages(messagePage.getTotalPages())
                .totalItems(totalItems)
                .build();

        return PaginationResponse.<MessageResponseDto>builder()
                .data(data)
                .meta(meta)
                .build();
    }

    @Override
    public MessageResponseDto sendMessage(UUID senderId, UUID receiverId, String content) {
        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException("Cannot send a message to yourself");
        }

        ConversationResponseDto conversation = conversationService
                .getOrCreateConversation(senderId, receiverId);

        Message message = Message.builder()
                .conversationId(conversation.getId())
                .senderId(senderId)
                .receiverId(receiverId)
                .content(content)
                .build();

        Message savedMessage = messageRepository.save(message);

        return MessageMapper.toDto(savedMessage);
    }
}
