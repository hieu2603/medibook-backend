package com.sgu.chat_service.security;

import com.sgu.chat_service.model.Conversation;
import com.sgu.chat_service.repository.ConversationRepository;
import com.sgu.common.exception.AccessDeniedException;
import com.sgu.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChatPermissionValidator {

    private final ConversationRepository conversationRepository;

    public void validateGetMessages(UUID conversationId, UUID currentUserId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));

        UUID participant1 = conversation.getParticipant1();
        UUID participant2 = conversation.getParticipant2();

        if (!participant1.equals(currentUserId) && !participant2.equals(currentUserId)) {
            throw new AccessDeniedException("You are not allowed to access this conversation");
        }
    }
}
