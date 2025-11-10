package com.sgu.chat_service.repository;

import com.sgu.chat_service.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {
    @Query("SELECT c FROM Conversation c WHERE (c.participant1 = :u1 AND c.participant2 = :u2) OR (c.participant1 = :u2 AND c.participant2 = :u1)")
    Optional<Conversation> findConversationBetween(@Param("u1") UUID u1, @Param("u2") UUID u2);
}
