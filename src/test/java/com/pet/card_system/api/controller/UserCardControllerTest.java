package com.pet.card_system.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet.card_system.core.dto.*;
import com.pet.card_system.core.repository.entity.CardActionType;
import com.pet.card_system.core.repository.entity.CardStatus;
import com.pet.card_system.security.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserCardControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CardService cardService;

    @InjectMocks
    private UserCardController userCardController;

    private CardDTO mockCardDTO;
    private CardActionRequestCreateDTO mockRequestDTO;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        mockCardDTO = new CardDTO(
                1L,
                "1234",
                "John Doe",
                "12/23",
                CardStatus.ACTIVE,
                BigDecimal.valueOf(1000)
        );
        mockRequestDTO = new CardActionRequestCreateDTO(
                "Block card due to suspicious activity",
                CardActionType.BLOCK
        );
        mockMvc = MockMvcBuilders.standaloneSetup(userCardController).build();
    }

    @Test
    void testRequestBlockCard() throws Exception {
        Long cardId = 1L;

        String requestJson = objectMapper.writeValueAsString(mockRequestDTO);

        mockMvc.perform(post("/api/cards/{cardId}/action", cardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated());

        verify(cardService, times(1)).createRequest(eq(cardId), eq(mockRequestDTO));
    }
}
