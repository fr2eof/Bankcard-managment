package com.pet.card_system.api.controller;

import com.pet.card_system.core.dto.CardActionRequestProcessDTO;
import com.pet.card_system.core.dto.CardCreateRequest;
import com.pet.card_system.core.dto.CardDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/cards")
@RequiredArgsConstructor
@Tag(name = "Admin - Cards", description = "Admin management of bank cards")
public class AdminCardController {
    private final CardService cardService;

    @Operation(summary = "Create a new card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping
    public ResponseEntity<CardDTO> createCard(@RequestBody @Valid CardCreateRequest request) {
        CardDTO cardDTO = cardService.createCard(request);
        return ResponseEntity.ok(cardDTO);
    }

    @Operation(summary = "Update card status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status")
    })
    @PutMapping("/{cardId}/status")
    public ResponseEntity<CardDTO> updateCardStatus(
            @PathVariable Long cardId,
            @RequestParam String status) {
        CardStatus newStatus = CardStatus.valueOf(status);
        CardDTO cardDTO = cardService.updateCardStatus(cardId, newStatus);
        return ResponseEntity.ok(cardDTO);
    }

    @Operation(summary = "Delete a card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Card not found")
    })
    @DeleteMapping("/{cardId}")
    public ResponseEntity<String> deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
        return ResponseEntity.ok("Card deleted successfully.");
    }

    @Operation(summary = "Get all cards with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of cards"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public Page<CardDTO> getAllCards(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return cardService.getAllCards(pageable);
    }

    @Operation(summary = "Process a card action request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request processed successfully"),
            @ApiResponse(responseCode = "404", description = "Request not found")
    })
    @PatchMapping("/{requestId}/process")
    public ResponseEntity<CardDTO> processRequest(
            @PathVariable Long requestId,
            @RequestBody @Valid CardActionRequestProcessDTO requestDto) {
        CardDTO cardDTO = cardService.processRequest(requestId, requestDto);
        return ResponseEntity.ok(cardDTO);
    }
}
