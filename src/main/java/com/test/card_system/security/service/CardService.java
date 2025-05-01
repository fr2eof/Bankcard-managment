package com.test.card_system.security.service;

import com.test.card_system.core.dto.CardCreateRequest;
import com.test.card_system.core.dto.CardDTO;
import com.test.card_system.core.repository.entity.CardBlockRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CardService {

    CardDTO createCard(CardCreateRequest request, Long userId);

    List<CardDTO> getUserCards(Long userId);

    Page<CardDTO> getAllCards(Pageable pageable);

    CardDTO blockCard(Long cardId);

    CardDTO processBlockRequest(Long requestId, boolean approve, String adminComment);

    CardDTO unblockCard(Long cardId);

    void deleteCard(Long cardId);

    CardBlockRequest requestBlockCard(Long cardId, String comment);

    CardDTO getCardDetails(Long cardId);

}
