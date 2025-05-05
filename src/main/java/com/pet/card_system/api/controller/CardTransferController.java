package com.pet.card_system.api.controller;


import com.pet.card_system.core.dto.CardTransferRequestDTO;
import com.pet.card_system.security.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class CardTransferController {

    private final CardService cardService;

    @PostMapping
    public ResponseEntity<Void> transferBetweenCards(@RequestBody @Valid CardTransferRequestDTO transferRequest) {
        cardService.transferBetweenCards(transferRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

