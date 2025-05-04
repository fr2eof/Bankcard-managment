package com.pet.card_system.api.controller;

import com.pet.card_system.core.dto.CardActionRequestProcessDTO;
import com.pet.card_system.core.dto.CardCreateRequest;
import com.pet.card_system.core.dto.CardDTO;
import com.pet.card_system.core.repository.entity.CardStatus;
import com.pet.card_system.security.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/cards")
@RequiredArgsConstructor
public class AdminCardController {
    private final CardService cardService;

    @PostMapping
    public ResponseEntity<CardDTO> createCard(@RequestBody CardCreateRequest request) {
        CardDTO cardDTO = cardService.createCard(request);
        return ResponseEntity.ok(cardDTO);
    }

    @PutMapping("/{cardId}/status")
    public ResponseEntity<CardDTO> updateCardStatus(
            @PathVariable Long cardId,
            @RequestParam CardStatus status) {
        CardDTO cardDTO = cardService.updateCardStatus(cardId, status);
        return ResponseEntity.ok(cardDTO);
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<String> deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
        return ResponseEntity.ok("Card deleted successfully.");
    }

    @GetMapping
    public Page<CardDTO> getAllCards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        Sort.Direction direction = sort[1].equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(direction, sort[0])
        );

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
