package com.sgu.chat_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "conversations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "conversation_id")
    private UUID id;

    @Column(name = "participant_1", nullable = false)
    private UUID participant1;

    @Column(name = "participant_2", nullable = false)
    private UUID participant2;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
