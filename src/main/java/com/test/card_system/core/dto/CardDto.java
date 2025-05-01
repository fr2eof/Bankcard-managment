package com.test.card_system.core.dto;

import com.test.card_system.core.repository.entity.CardStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class CardDto {
    private Long id;
    private String cardNumberMasked;
    private String ownerName;
    private LocalDate expirationDate;
    private CardStatus status;
    private BigDecimal balance;
}
