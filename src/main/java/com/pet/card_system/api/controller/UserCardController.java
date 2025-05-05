package com.pet.card_system.api.controller;

import com.pet.card_system.core.dto.*;
import com.pet.card_system.core.repository.entity.CardStatus;
import com.pet.card_system.security.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class UserCardController {
    private final CardService cardService;

    @GetMapping("/{userId}")
    public Page<CardDTO> getUserCards(
            @PathVariable Long userId,
            @RequestParam(required = false) String cardType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) BigDecimal minBalance,
            @RequestParam(required = false) BigDecimal maxBalance,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        CardSearchCriteria criteria = CardSearchCriteria.builder()
                .cardType(cardType)
                .status(CardStatus.valueOf(status))
                .minBalance(minBalance)
                .maxBalance(maxBalance)
                .build();

        return cardService.getUserCards(userId, criteria, pageable);
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<CardDTO> getCardDetails(@PathVariable Long cardId) {
        CardDTO card = cardService.getCardDetails(cardId);
        return ResponseEntity.ok(card);
    }

    @PostMapping("/{cardId}/action")
    public ResponseEntity<Void> requestBlockCard(
            @PathVariable Long cardId,
            @RequestBody @Valid CardActionRequestCreateDTO requestCreateDTO
    ) {
        cardService.createRequest(cardId, requestCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transferBetweenCards(
            @RequestBody TransferRequest request) {
        return ResponseEntity.ok().body(new TransferResponse());
        // Перевод между картами пользователя
    }
}