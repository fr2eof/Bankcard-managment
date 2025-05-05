package com.pet.card_system.core.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CardTransferRequestDTO {

    @NotNull
    private Long sourceCardId;

    @NotNull
    private Long targetCardId;

    @NotNull
    @Positive
    private BigDecimal amount;

    private String description;
}

