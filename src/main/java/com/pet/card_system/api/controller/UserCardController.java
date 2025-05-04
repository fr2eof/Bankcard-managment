package com.pet.card_system.api.controller;

import com.pet.card_system.core.dto.CardDTO;
import com.pet.card_system.core.dto.TransferRequest;
import com.pet.card_system.core.dto.TransferResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class UserCardController {

    @GetMapping
    public ResponseEntity<List<CardDTO>> getUserCards(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return null;
        // Логика получения карт пользователя
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardDTO> getCardDetails(@PathVariable Long id) {
        return null;
        // Получение деталей конкретной карты
    }

    @PostMapping("/{id}/block")
    public ResponseEntity<Void> requestBlockCard(@PathVariable Long id) {
        return ResponseEntity.ok().build();
        // Запрос на блокировку карты
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transferBetweenCards(
            @RequestBody TransferRequest request) {
        return ResponseEntity.ok().body(new TransferResponse());
        // Перевод между картами пользователя
    }
}
/*
*
*
*
* Просматривать свои карты (с параметризованным поиском и постраничной выдачей).
Запрашивать блокировку карты.
Совершать переводы между своими картами, если у него есть несколько карт.
Просматривать баланс по своим картам.
*
*
* ✅ API:
Эндпоинты для CRUD-операций над картами.
Операции перевода средств между картами пользователя.
Параметризированный поиск и постраничная выдача списка карт.
Валидация входящих данных.
*
*
* */