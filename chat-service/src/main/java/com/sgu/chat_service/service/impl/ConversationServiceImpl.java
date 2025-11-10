package com.sgu.chat_service.service.impl;

import com.sgu.chat_service.dto.response.ConversationResponseDto;
import com.sgu.chat_service.mapper.ConversationMapper;
import com.sgu.chat_service.model.Conversation;
import com.sgu.chat_service.repository.ConversationRepository;
import com.sgu.chat_service.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;

    @Override
    public ConversationResponseDto getOrCreateConversation(UUID user1, UUID user2) {
        return conversationRepository.findConversationBetween(user1, user2)
                .map(ConversationMapper::toDto)
                .orElseGet(() -> {
                    Conversation conversation = Conversation.builder()
                            .participant1(user1)
                            .participant2(user2)
                            .build();

                    Conversation newConversation = conversationRepository.save(conversation);
                    return ConversationMapper.toDto(newConversation);
                });
    }
}
