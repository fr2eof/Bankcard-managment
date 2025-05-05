package com.pet.card_system.api.controller;

import com.pet.card_system.core.dto.CardActionRequestProcessDTO;
import com.pet.card_system.core.dto.CardCreateRequest;
import com.pet.card_system.core.dto.CardDTO;
import com.pet.card_system.core.repository.entity.CardStatus;
import com.pet.card_system.security.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpServerErrorException;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminCardControllerTest {
    @Mock
    private CardService cardService;

    @InjectMocks
    private AdminCardController adminCardController;

    private CardDTO mockCardDTO;
    private CardCreateRequest mockCreateRequest;
    private CardActionRequestProcessDTO mockProcessDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockCardDTO = CardDTO.builder()
                .id(1L)
                .numberMasked("**** **** **** 1234")
                .holderName("JOHN DOE")
                .formattedExpiryDate("12/26")
                .status(CardStatus.ACTIVE)
                .balance(new BigDecimal("100.00"))
                .build();

        mockCreateRequest = new CardCreateRequest(
                "encryptedDataExampleEncryptedDataExampleEncryptedDataExample1234",
                "1234",
                "JOHN DOE",
                LocalDate.now().plusYears(1),
                10L
        );

        mockProcessDTO = new CardActionRequestProcessDTO(true, "Approved after review.");
    }


    @Test
    public void testCreateCard() {
        when(cardService.createCard(any(CardCreateRequest.class))).thenReturn(mockCardDTO);

        ResponseEntity<CardDTO> response = adminCardController.createCard(mockCreateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCardDTO, response.getBody());
        verify(cardService, times(1)).createCard(any(CardCreateRequest.class));
    }

    @Test
    public void testUpdateCardStatus() {
        when(cardService.updateCardStatus(eq(1L), any(CardStatus.class))).thenReturn(mockCardDTO);

        ResponseEntity<CardDTO> response = adminCardController.updateCardStatus(1L, "ACTIVE");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCardDTO, response.getBody());
        verify(cardService, times(1)).updateCardStatus(eq(1L), any(CardStatus.class));
    }

    @Test
    public void testDeleteCard() {
        doNothing().when(cardService).deleteCard(1L);

        ResponseEntity<String> response = adminCardController.deleteCard(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Card deleted successfully.", response.getBody());
        verify(cardService, times(1)).deleteCard(1L);
    }

    @Test
    public void testGetAllCards() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());

        @SuppressWarnings("unchecked")
        Page<CardDTO> mockPage = (Page<CardDTO>) mock(Page.class);
        when(cardService.getAllCards(pageable)).thenReturn(mockPage);

        Page<CardDTO> response = adminCardController.getAllCards(pageable);

        assertNotNull(response);
        verify(cardService, times(1)).getAllCards(pageable);
    }


    @Test
    public void testProcessRequest() {
        when(cardService.processRequest(eq(1L), any(CardActionRequestProcessDTO.class))).thenReturn(mockCardDTO);

        ResponseEntity<CardDTO> response = adminCardController.processRequest(1L, mockProcessDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCardDTO, response.getBody());
        verify(cardService, times(1)).processRequest(eq(1L), any(CardActionRequestProcessDTO.class));
    }

    @Test
    public void testProcessRequestThrowsException() {
        when(cardService.processRequest(eq(1L), any(CardActionRequestProcessDTO.class)))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing request"));

        assertThrows(HttpServerErrorException.class, () -> {
            adminCardController.processRequest(1L, mockProcessDTO);
        });

        verify(cardService, times(1)).processRequest(eq(1L), any(CardActionRequestProcessDTO.class));
    }

}

