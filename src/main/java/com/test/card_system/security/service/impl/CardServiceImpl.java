package com.test.card_system.security.service.impl;

import com.test.card_system.core.dto.CardCreateRequest;
import com.test.card_system.core.dto.CardDTO;
import com.test.card_system.core.exception.CardNotFoundException;
import com.test.card_system.core.exception.RequestNotFoundException;
import com.test.card_system.core.exception.UserNotFoundException;
import com.test.card_system.core.mapper.CardMapper;
import com.test.card_system.core.repository.CardBlockRequestRepository;
import com.test.card_system.core.repository.CardRepository;
import com.test.card_system.core.repository.UserRepository;
import com.test.card_system.core.repository.entity.*;
import com.test.card_system.security.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardMapper cardMapper;

    @Override
    @Transactional
    public CardDTO createCard(CardCreateRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found"));

        Card card = Card.builder()
                .numberEncrypted(request.encryptedCardData())
                .numberMasked(request.lastFourDigits())
                .numberMasked(request.cardHolder().toUpperCase())
                .expiryDate(request.expiryDate())
                .status(CardStatus.ACTIVE)
                .balance(BigDecimal.ZERO)
                .user(user)
                .build();

        return cardMapper.toDTO(cardRepository.save(card));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardDTO> getUserCards(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found"));

        return cardRepository.findByUserId(userId).stream()
                .map(cardMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardDTO> getAllCards(Pageable pageable) {
        return cardRepository.findAll(pageable).map(cardMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public CardDTO getCardDetails(Long cardId) {
        return cardMapper.toDTO(cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card with id: " + cardId + " not found")));
    }

    @Override
    @Transactional
    public CardDTO blockCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card with id: " + cardId + " not found"));

        card.setStatus(CardStatus.BLOCKED);
        return cardMapper.toDTO(cardRepository.save(card));
    }

    @Override
    @Transactional
    public CardDTO unblockCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card with id: " + cardId + " not found"));

        if (card.getExpiryDate().isBefore(LocalDate.now())) {
            card.setStatus(CardStatus.EXPIRED);
        } else {
            card.setStatus(CardStatus.ACTIVE);
        }

        return cardMapper.toDTO(cardRepository.save(card));
    }

    @Override
    @Transactional
    public void deleteCard(Long cardId) {
        cardRepository.deleteById(cardId);
    }

    private final CardBlockRequestRepository blockRequestRepository;

    @Override
    public CardBlockRequest requestBlockCard(Long cardId, String comment) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card with id: " + cardId + " not found"));

        CardBlockRequest request = CardBlockRequest.builder()
                .card(card)
                .status(BlockRequestStatus.PENDING)
                .userComment(comment)
                .build();

        return blockRequestRepository.save(request);
    }

    @Override
    @Transactional
    public CardDTO processBlockRequest(Long requestId, boolean approve, String adminComment) {
        CardBlockRequest request = blockRequestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException("Request with id: " + requestId + " not found"));

        request.setStatus(approve ? BlockRequestStatus.APPROVED : BlockRequestStatus.REJECTED);
        request.setAdminComment(adminComment);

        Card card = request.getCard();
        if (approve) {
            request.getCard().setStatus(CardStatus.BLOCKED);
            card = cardRepository.save(request.getCard());
        }

        return cardMapper.toDTO(card);
    }
}
