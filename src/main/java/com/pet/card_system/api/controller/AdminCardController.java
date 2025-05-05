package com.pet.card_system.api.controller;

import com.pet.card_system.core.dto.CardActionRequestProcessDTO;
import com.pet.card_system.core.dto.CardCreateRequest;
import com.pet.card_system.core.dto.CardDTO;
import com.pet.card_system.core.dto.CardSearchCriteria;
import com.pet.card_system.core.repository.entity.CardStatus;
import com.pet.card_system.security.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/cards")
@RequiredArgsConstructor
public class AdminCardController {
    private final CardService cardService;

    @PostMapping
    public ResponseEntity<CardDTO> createCard(@RequestBody @Valid CardCreateRequest request) {
        CardDTO cardDTO = cardService.createCard(request);
        return ResponseEntity.ok(cardDTO);
    }

    @PutMapping("/{cardId}/status")
    public ResponseEntity<CardDTO> updateCardStatus(
            @PathVariable Long cardId,
            @RequestParam String status) {
        CardStatus newStatus = CardStatus.valueOf(status);
        CardDTO cardDTO = cardService.updateCardStatus(cardId, newStatus);
        return ResponseEntity.ok(cardDTO);
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<String> deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
        return ResponseEntity.ok("Card deleted successfully.");
    }

    @GetMapping
    public Page<CardDTO> getAllCards(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        return cardService.getAllCards(pageable);
    }

    @PatchMapping("/{requestId}/process")
    public ResponseEntity<CardDTO> processRequest(
            @PathVariable Long requestId,
            @RequestBody @Valid CardActionRequestProcessDTO requestDto) {
        CardDTO cardDTO = cardService.processRequest(
                requestId,
                requestDto
        );
        return ResponseEntity.ok(cardDTO);
    }
}
