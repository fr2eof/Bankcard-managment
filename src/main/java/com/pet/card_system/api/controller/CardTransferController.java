package com.pet.card_system.api.controller;


import com.pet.card_system.core.dto.CardTransferRequestDTO;
import com.pet.card_system.security.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
@Tag(name = "Transfers", description = "Operations related to card transfers")
public class CardTransferController {

    private final CardService cardService;

    @Operation(summary = "Transfer money between user cards")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transfer successful"),
            @ApiResponse(responseCode = "400", description = "Invalid transfer request")
    })
    @PostMapping
    public ResponseEntity<Void> transferBetweenCards(@RequestBody @Valid CardTransferRequestDTO transferRequest) {
        cardService.transferBetweenCards(transferRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

