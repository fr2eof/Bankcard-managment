package com.pet.card_system.core.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "card_action_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardActionRequest {
    @Id
    private Long id;

    @ManyToOne
    private Card card;

    @Enumerated(EnumType.STRING)
    private CardActionType actionType;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private String userComment;
    private String adminComment;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}


