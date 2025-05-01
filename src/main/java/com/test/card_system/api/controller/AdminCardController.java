package com.test.card_system.api.controller;

import com.test.card_system.core.dto.CardCreateRequest;
import com.test.card_system.core.dto.CardDTO;
import com.test.card_system.core.repository.entity.Card;
import com.test.card_system.core.repository.entity.CardStatus;
import com.test.card_system.security.service.CardService;
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
        // Создание новой карты (админ)
        return null;
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<CardDTO> updateCardStatus(
            @PathVariable Long id,
            @RequestParam CardStatus status) {
        // Изменение статуса карты
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        // Удаление карты
        return ResponseEntity.noContent().build();
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
}
