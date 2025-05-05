package com.pet.card_system.api.controller;

import com.pet.card_system.core.dto.*;
import com.pet.card_system.core.repository.entity.CardStatus;
import com.pet.card_system.security.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
@Tag(name = "User - Cards", description = "User management of cards")
public class UserCardController {
    private final CardService cardService;

    @Operation(summary = "Get user's cards with filters and pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of user's cards"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
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

    @Operation(summary = "Get card details by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card details"),
            @ApiResponse(responseCode = "404", description = "Card not found")
    })
    @GetMapping("/{cardId}")
    public ResponseEntity<CardDTO> getCardDetails(@PathVariable Long cardId) {
        CardDTO card = cardService.getCardDetails(cardId);
        return ResponseEntity.ok(card);
    }

    @Operation(summary = "Request to block a card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Block request created"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("/{cardId}/action")
    public ResponseEntity<Void> requestBlockCard(
            @PathVariable Long cardId,
            @RequestBody @Valid CardActionRequestCreateDTO requestCreateDTO) {
        cardService.createRequest(cardId, requestCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Get the balance of a card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card balance"),
            @ApiResponse(responseCode = "404", description = "Card not found")
    })
    @GetMapping("/{cardId}/balance")
    public ResponseEntity<BigDecimal> getCardBalance(@PathVariable Long cardId) {
        BigDecimal balance = cardService.getCardBalance(cardId);
        return ResponseEntity.ok(balance);
    }

    @Operation(summary = "Get all card balances for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of card balances"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{userId}/balances")
    public ResponseEntity<List<CardBalanceDTO>> getUserCardBalances(@PathVariable Long userId) {
        List<CardBalanceDTO> balances = cardService.getUserCardBalances(userId);
        return ResponseEntity.ok(balances);
    }
}
