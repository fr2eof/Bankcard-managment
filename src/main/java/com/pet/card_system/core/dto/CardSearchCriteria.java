package com.pet.card_system.core.dto;

import com.pet.card_system.core.repository.entity.CardStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class CardSearchCriteria {
    private String cardType; // visa, mastercard, mir
    private CardStatus status;
    private BigDecimal minBalance;
    private BigDecimal maxBalance;
}
