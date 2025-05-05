package com.pet.card_system.security.service.impl;

import com.pet.card_system.core.dto.*;
import com.pet.card_system.core.exception.CardNotFoundException;
import com.pet.card_system.core.exception.UserNotFoundException;
import com.pet.card_system.core.mapper.CardMapper;
import com.pet.card_system.core.repository.CardActionRequestRepository;
import com.pet.card_system.core.repository.CardRepository;
import com.pet.card_system.core.repository.UserRepository;
import com.pet.card_system.core.repository.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CardMapper cardMapper;

    @Mock
    private CardActionRequestRepository requestRepository;

    @InjectMocks
    private CardServiceImpl cardService;

    private User testUser;
    private Card testCard;
    private CardDTO testCardDTO;
    private CardActionRequest testRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);

        testCard = Card.builder()
                .id(1L)
                .numberEncrypted("encrypted_1234")
                .numberMasked("1234")
                .holderName("TEST USER")
                .expiryDate(LocalDate.now().plusYears(2))
                .status(CardStatus.ACTIVE)
                .balance(BigDecimal.valueOf(1000))
                .user(testUser)
                .build();

        testCardDTO = CardDTO.builder()
                .id(1L)
                .numberMasked("1234")
                .holderName("TEST USER")
                .status(CardStatus.ACTIVE)
                .balance(BigDecimal.valueOf(1000))
                .build();

        testRequest = CardActionRequest.builder()
                .id(1L)
                .card(testCard)
                .actionType(CardActionType.BLOCK)
                .status(RequestStatus.PENDING)
                .userComment("Please block my card")
                .build();
    }


    @Test
    void createCard_ShouldSuccessfullyCreateCard() {

        CardCreateRequest request = new CardCreateRequest(
                "encrypted_1234", "1234", "Test User",
                LocalDate.now().plusYears(2), 1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cardRepository.save(any(Card.class))).thenReturn(testCard);
        when(cardMapper.toDTO(testCard)).thenReturn(testCardDTO);


        CardDTO result = cardService.createCard(request);


        assertNotNull(result);
        assertEquals("1234", result.getNumberMasked());
        assertEquals("TEST USER", result.getHolderName());
        verify(userRepository).findById(1L);
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    void createCard_ShouldThrowUserNotFoundException() {

        CardCreateRequest request = new CardCreateRequest(
                "encrypted_1234", "1234", "Test User",
                LocalDate.now().plusYears(2), 99L);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> cardService.createCard(request));
        verify(userRepository).findById(99L);
        verifyNoInteractions(cardRepository, cardMapper);
    }

    @Test
    void createCard_ShouldHandleExpiredCard() {

        CardCreateRequest request = new CardCreateRequest(
                "encrypted_1234", "1234", "Test User",
                LocalDate.now().minusDays(1), 1L);

        assertThrows(IllegalArgumentException.class, () -> cardService.createCard(request));
    }


    @Test
    void getUserCards_ShouldReturnCardsForUser() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<Card> cardPage = new PageImpl<>(List.of(testCard));

        when(userRepository.existsById(1L)).thenReturn(true);
        when(cardRepository.findByUserId(1L, pageable)).thenReturn(cardPage);
        when(cardMapper.toDTO(testCard)).thenReturn(testCardDTO);


        Page<CardDTO> result = cardService.getUserCards(1L, pageable);


        assertEquals(1, result.getContent().size());
        assertEquals(testCardDTO, result.getContent().get(0));
        verify(userRepository).existsById(1L);
        verify(cardRepository).findByUserId(1L, pageable);
    }

    @Test
    void getUserCards_ShouldThrowUserNotFoundException() {

        Pageable pageable = PageRequest.of(0, 10);
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> cardService.getUserCards(99L, pageable));
    }

    @Test
    void getUserCardsWithCriteria_ShouldApplyFilters() {

        Pageable pageable = PageRequest.of(0, 10);
        CardSearchCriteria criteria = CardSearchCriteria.builder()
                .status(CardStatus.ACTIVE)
                .minBalance(BigDecimal.valueOf(500))
                .maxBalance(BigDecimal.valueOf(1500))
                .build();
        Page<Card> cardPage = new PageImpl<>(List.of(testCard));

        when(userRepository.existsById(1L)).thenReturn(true);
        when(cardRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(cardPage);
        when(cardMapper.toDTO(testCard)).thenReturn(testCardDTO);


        Page<CardDTO> result = cardService.getUserCards(1L, criteria, pageable);


        assertEquals(1, result.getContent().size());
        verify(cardRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void getUserCardsWithCriteria_ShouldThrowForInvalidBalanceRange() {

        Pageable pageable = PageRequest.of(0, 10);
        CardSearchCriteria criteria = CardSearchCriteria.builder()
                .status(null)
                .minBalance(BigDecimal.valueOf(1500))
                .maxBalance(BigDecimal.valueOf(500))
                .build();

        when(userRepository.existsById(1L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> cardService.getUserCards(1L, criteria, pageable));
    }

    @Test
    void getCardDetails_ShouldReturnCard() {

        when(cardRepository.findById(1L)).thenReturn(Optional.of(testCard));
        when(cardMapper.toDTO(testCard)).thenReturn(testCardDTO);


        CardDTO result = cardService.getCardDetails(1L);


        assertEquals(testCardDTO, result);
        verify(cardRepository).findById(1L);
    }

    @Test
    void getCardDetails_ShouldThrowCardNotFoundException() {

        when(cardRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () -> cardService.getCardDetails(99L));
    }

    @Test
    void blockCard_ShouldBlockActiveCard() {

        Card blockedCard = Card.builder()
                .id(1L)
                .status(CardStatus.BLOCKED)
                .build();
        CardDTO blockedCardDTO = CardDTO.builder()
                .id(1L)
                .status(CardStatus.BLOCKED)
                .build();

        when(cardRepository.findById(1L)).thenReturn(Optional.of(testCard));
        when(cardRepository.save(any(Card.class))).thenReturn(blockedCard);
        when(cardMapper.toDTO(blockedCard)).thenReturn(blockedCardDTO);


        CardDTO result = cardService.blockCard(1L);


        assertEquals(CardStatus.BLOCKED, result.getStatus());
        verify(cardRepository).save(argThat(card -> card.getStatus() == CardStatus.BLOCKED));
    }

    @Test
    void unblockCard_ShouldUnblockCard() {

        when(cardRepository.findById(1L)).thenReturn(Optional.of(testCard));
        when(cardRepository.save(any(Card.class))).thenReturn(testCard);
        when(cardMapper.toDTO(testCard)).thenReturn(testCardDTO);


        CardDTO result = cardService.unblockCard(1L);


        assertEquals(CardStatus.ACTIVE, result.getStatus());
    }

    @Test
    void unblockCard_ShouldSetExpiredStatusIfDatePassed() {

        Card expiredCard = Card.builder()
                .id(1L)
                .expiryDate(LocalDate.now().minusDays(1))
                .status(CardStatus.BLOCKED)
                .build();
        CardDTO expiredCardDTO = CardDTO.builder()
                .id(1L)
                .status(CardStatus.EXPIRED)
                .build();

        when(cardRepository.findById(1L)).thenReturn(Optional.of(expiredCard));
        when(cardRepository.save(any(Card.class))).thenReturn(expiredCard);
        when(cardMapper.toDTO(expiredCard)).thenReturn(expiredCardDTO);


        CardDTO result = cardService.unblockCard(1L);


        assertEquals(CardStatus.EXPIRED, result.getStatus());
    }

    @Test
    void updateCardStatus_ShouldDelegateToBlockOrUnblock() {

        when(cardRepository.findById(1L)).thenReturn(Optional.of(testCard));
        when(cardRepository.save(any(Card.class))).thenReturn(testCard);
        when(cardMapper.toDTO(testCard)).thenReturn(testCardDTO);


        CardDTO blockResult = cardService.updateCardStatus(1L, CardStatus.BLOCKED);
        assertEquals(CardStatus.ACTIVE, blockResult.getStatus());


        CardDTO unblockResult = cardService.updateCardStatus(1L, CardStatus.ACTIVE);
        assertEquals(CardStatus.ACTIVE, unblockResult.getStatus());

        assertThrows(IllegalArgumentException.class,
                () -> cardService.updateCardStatus(1L, CardStatus.EXPIRED));
    }

    @Test
    void createRequest_ShouldCreatePendingRequest() {

        CardActionRequestCreateDTO requestDTO = new CardActionRequestCreateDTO(
                "Please block my card", CardActionType.BLOCK);

        when(cardRepository.findById(1L)).thenReturn(Optional.of(testCard));


        cardService.createRequest(1L, requestDTO);


        verify(requestRepository).save(argThat(request ->
                request.getStatus() == RequestStatus.PENDING &&
                        request.getActionType() == CardActionType.BLOCK));
    }

    @Test
    void processRequest_ShouldApproveAndBlockCard() {

        CardActionRequestProcessDTO processDTO = new CardActionRequestProcessDTO(
                true, "Approved by admin");
        Card blockedCard = Card.builder()
                .id(1L)
                .status(CardStatus.BLOCKED)
                .build();
        CardDTO blockedCardDTO = CardDTO.builder()
                .id(1L)
                .status(CardStatus.BLOCKED)
                .build();

        when(requestRepository.findById(1L)).thenReturn(Optional.of(testRequest));
        when(cardRepository.save(any(Card.class))).thenReturn(blockedCard);
        when(cardMapper.toDTO(blockedCard)).thenReturn(blockedCardDTO);


        CardDTO result = cardService.processRequest(1L, processDTO);


        assertEquals(CardStatus.BLOCKED, result.getStatus());
        verify(requestRepository).save(argThat(request ->
                request.getStatus() == RequestStatus.APPROVED));
    }

    @Test
    void processRequest_ShouldRejectRequest() {
        CardActionRequestProcessDTO processDTO = new CardActionRequestProcessDTO(
                false, "Rejected by admin");

        when(requestRepository.findById(1L)).thenReturn(Optional.of(testRequest));
        when(cardMapper.toDTO(testRequest.getCard())).thenReturn(testCardDTO);

        CardDTO result = cardService.processRequest(1L, processDTO);

        assertNotNull(result);
        assertEquals(CardStatus.ACTIVE, result.getStatus());
        verify(requestRepository).save(argThat(request ->
                request.getStatus() == RequestStatus.REJECTED));
        verify(cardRepository, never()).save(any());
    }

    @Test
    void deleteCard_ShouldDeleteCard() {

        cardService.deleteCard(1L);


        verify(cardRepository).deleteById(1L);
    }

    @Test
    void getAllCards_ShouldReturnAllCards() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<Card> cardPage = new PageImpl<>(List.of(testCard));

        when(cardRepository.findAll(pageable)).thenReturn(cardPage);
        when(cardMapper.toDTO(testCard)).thenReturn(testCardDTO);


        Page<CardDTO> result = cardService.getAllCards(pageable);


        assertEquals(1, result.getContent().size());
        assertEquals(testCardDTO, result.getContent().get(0));
    }

    @Test
    void createCard_ShouldThrowException_WhenUserIdIsNull() {
        CardCreateRequest request = new CardCreateRequest(
                "encrypted_1234", "1234", "Test User",
                LocalDate.now().plusYears(2), null);

        assertThrows(UserNotFoundException.class, () -> cardService.createCard(request));
    }

    @Test
    void createCard_ShouldNotPersist_WhenExceptionAfterSave() {
        CardCreateRequest request = new CardCreateRequest(
                "encrypted_1234", "1234", "Test User",
                LocalDate.now().plusYears(2), 1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cardRepository.save(any())).thenThrow(new RuntimeException("Simulated failure"));

        assertThrows(RuntimeException.class, () -> cardService.createCard(request));

    }

    @Test
    void processRequest_ShouldUnblockCard_WhenTypeIsUnblock() {
        CardActionRequest unblockRequest = CardActionRequest.builder()
                .id(1L)
                .card(testCard)
                .actionType(CardActionType.UNBLOCK)
                .status(RequestStatus.PENDING)
                .build();

        CardActionRequestProcessDTO dto = new CardActionRequestProcessDTO(true, "Unblock approved");

        when(requestRepository.findById(1L)).thenReturn(Optional.of(unblockRequest));
        when(cardRepository.save(any())).thenReturn(testCard);
        when(cardMapper.toDTO(testCard)).thenReturn(testCardDTO);

        CardDTO result = cardService.processRequest(1L, dto);

        assertEquals(CardStatus.ACTIVE, result.getStatus());
    }


}