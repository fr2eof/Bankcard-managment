package com.pet.card_system.security.service;

import com.pet.card_system.core.dto.*;
import com.pet.card_system.core.repository.entity.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardService {

    CardDTO createCard(CardCreateRequest request);

    Page<CardDTO> getAllCards(Pageable pageable);

    CardDTO updateCardStatus(Long cardId, CardStatus status);

    CardDTO blockCard(Long cardId);

    CardDTO unblockCard(Long cardId);

    CardDTO processRequest(Long requestId, CardActionRequestProcessDTO request);

    void deleteCard(Long cardId);

    Page<CardDTO> getUserCards(Long userId, Pageable pageable);

    Page<CardDTO> getUserCards(Long userId, CardSearchCriteria criteria, Pageable pageable);

    void createRequest(Long cardId, CardActionRequestCreateDTO blockRequestDTO);

    CardDTO getCardDetails(Long cardId);

}
