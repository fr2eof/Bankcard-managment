package com.test.card_system.core.dto;

import com.test.card_system.core.repository.entity.CardStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardDTO {
    private Long id;
    private String numberMasked;
    private String holderName;
    private String formattedExpiryDate;
    private CardStatus status;
    private BigDecimal balance;
}
