package com.pet.card_system.security.service;

import com.pet.card_system.core.dto.CardActionRequestCreateDTO;
import com.pet.card_system.core.dto.CardActionRequestProcessDTO;
import com.pet.card_system.core.repository.entity.CardActionRequest;
import com.pet.card_system.core.dto.CardCreateRequest;
import com.pet.card_system.core.dto.CardDTO;
import com.pet.card_system.core.repository.entity.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CardService {

    CardDTO createCard(CardCreateRequest request);

    Page<CardDTO> getAllCards(Pageable pageable);

    CardDTO updateCardStatus(Long cardId, CardStatus status);

    CardDTO blockCard(Long cardId);

    CardDTO unblockCard(Long cardId);

    CardDTO processRequest(Long requestId, CardActionRequestProcessDTO request);

    void deleteCard(Long cardId);

    List<CardDTO> getUserCards(Long userId);

    CardActionRequest createRequest(Long cardId, CardActionRequestCreateDTO blockRequestDTO);

    CardDTO getCardDetails(Long cardId);

}
