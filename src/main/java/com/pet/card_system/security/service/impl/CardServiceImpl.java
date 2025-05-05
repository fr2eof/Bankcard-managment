package com.pet.card_system.security.service.impl;

import com.pet.card_system.core.dto.*;
import com.pet.card_system.core.repository.entity.*;
import com.pet.card_system.core.exception.CardNotFoundException;
import com.pet.card_system.core.exception.RequestNotFoundException;
import com.pet.card_system.core.exception.UserNotFoundException;
import com.pet.card_system.core.mapper.CardMapper;
import com.pet.card_system.core.repository.CardActionRequestRepository;
import com.pet.card_system.core.repository.CardRepository;
import com.pet.card_system.core.repository.UserRepository;
import com.pet.card_system.security.service.CardService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardMapper cardMapper;
    private final CardActionRequestRepository requestRepository;


    @Override
    @Transactional
    public CardDTO createCard(CardCreateRequest request) {
        if (request.expiryDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Expiry date cannot be in the past");
        }
        Long userId = request.userId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found"));

        Card card = Card.builder()
                .numberEncrypted(request.encryptedCardData())
                .numberMasked(request.lastFourDigits())
                .holderName(request.cardHolder().toUpperCase())
                .expiryDate(request.expiryDate())
                .status(CardStatus.ACTIVE)
                .balance(BigDecimal.ZERO)
                .user(user)
                .build();

        return cardMapper.toDTO(cardRepository.save(card));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardDTO> getUserCards(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with id: " + userId + " not found");
        }

        Page<Card> cardsPage = cardRepository.findByUserId(userId, pageable);

        return cardsPage.map(cardMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardDTO> getUserCards(Long userId, CardSearchCriteria criteria, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with id: " + userId + " not found");
        }

        if (criteria.getMinBalance() != null && criteria.getMaxBalance() != null
                && criteria.getMinBalance().compareTo(criteria.getMaxBalance()) > 0) {
            throw new IllegalArgumentException("Invalid balance range");
        }

        Specification<Card> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("user").get("id"), userId));

            if (criteria.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), criteria.getStatus()));
            }
            if (criteria.getMinBalance() != null) {
                predicates.add(cb.ge(root.get("balance"), criteria.getMinBalance()));
            }
            if (criteria.getMaxBalance() != null) {
                predicates.add(cb.le(root.get("balance"), criteria.getMaxBalance()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return cardRepository.findAll(spec, pageable).map(cardMapper::toDTO);
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
    public CardDTO updateCardStatus(Long cardId, CardStatus status) {
        return switch (status) {
            case BLOCKED -> blockCard(cardId);
            case ACTIVE -> unblockCard(cardId);
            default -> throw new IllegalArgumentException("Unsupported card status: " + status);
        };
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

    @Override
    public void createRequest(Long cardId, CardActionRequestCreateDTO requestCreateDTO) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card with id: " + cardId + " not found"));

        CardActionRequest request = CardActionRequest.builder()
                .card(card)
                .actionType(requestCreateDTO.action())
                .status(RequestStatus.PENDING)
                .userComment(requestCreateDTO.userComment())
                .build();

        requestRepository.save(request);
    }


    @Override
    @Transactional
    public CardDTO processRequest(Long requestId, CardActionRequestProcessDTO requestDTO) {
        CardActionRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException("Request with id: " + requestId + " not found"));

        request.setStatus(requestDTO.approved() ? RequestStatus.APPROVED : RequestStatus.REJECTED);
        request.setAdminComment(requestDTO.adminComment());

        Card card = request.getCard();
        if (requestDTO.approved()) {
            if (request.getActionType() == CardActionType.BLOCK) {
                card.setStatus(CardStatus.BLOCKED);
            } else {
                if (card.getExpiryDate().isBefore(LocalDate.now())) {
                    card.setStatus(CardStatus.ACTIVE);
                } else {
                    card.setStatus(CardStatus.EXPIRED);
                }
            }
            card = cardRepository.save(request.getCard());
        }
        requestRepository.save(request);

        return cardMapper.toDTO(card);
    }
}
