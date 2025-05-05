package com.pet.card_system.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardBalanceDTO {
    private Long cardId;
    private String numberMasked;
    private BigDecimal balance;
}

