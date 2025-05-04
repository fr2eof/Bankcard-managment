//package com.pet.card_system.security.service.impl;
//
//import com.pet.card_system.core.dto.CardCreateRequest;
//import com.pet.card_system.core.dto.CardDTO;
//import com.pet.card_system.core.repository.CardActionRequestRepository;
//import com.pet.card_system.core.repository.CardRepository;
//import com.pet.card_system.core.repository.UserRepository;
//import com.pet.card_system.core.repository.entity.Card;
//import com.pet.card_system.core.repository.entity.CardStatus;
//import com.pet.card_system.core.repository.entity.RequestStatus;
//import com.pet.card_system.core.repository.entity.User;
//import com.test.card_system.core.dto.*;
//import com.pet.card_system.core.mapper.CardMapper;
//import com.test.card_system.core.repository.*;
//import com.test.card_system.core.repository.entity.*;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.*;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class CardServiceImplTest {
//
//    @Mock private CardRepository cardRepository;
//    @Mock private UserRepository userRepository;
//    @Mock private CardMapper cardMapper;
//    @Mock private CardActionRequestRepository blockRequestRepository;
//
//    @InjectMocks
//    private CardServiceImpl cardService;
//
//    @Test
//    void createCard_ShouldCreateCardSuccessfully() {
//        // Arrange
//        LocalDate expiryDate = LocalDate.now().plusYears(3);
//        CardCreateRequest request = new CardCreateRequest(
//                "encrypted_data_12345678901234567890123456789012",
//                "1234",
//                "JOHN DOE",
//                expiryDate,
//                1L
//        );
//
//        User user = new User();
//        user.setId(1L);
//
//        Card card = Card.builder()
//                .numberEncrypted(request.encryptedCardData())
//                .numberMasked(request.lastFourDigits())
//                .holderName(request.cardHolder())
//                .expiryDate(request.expiryDate())
//                .status(CardStatus.ACTIVE)
//                .balance(BigDecimal.ZERO)
//                .user(user)
//                .build();
//
//        CardDTO expectedDto = CardDTO.builder()
//                .id(1L)
//                .numberMasked("1234")
//                .holderName("JOHN DOE")
//                .status(CardStatus.ACTIVE)
//                .balance(BigDecimal.ZERO)
//                .build();
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//        when(cardRepository.save(any(Card.class))).thenReturn(card);
////        when(cardMapper.toDTO(card))).thenReturn(expectedDto);
//
//        // Act
//        CardDTO result = cardService.createCard(request);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals("1234", result.getNumberMasked());
//        assertEquals("JOHN DOE", result.getHolderName());
//        assertEquals(CardStatus.ACTIVE, result.getStatus());
//        verify(userRepository).findById(1L);
//        verify(cardRepository).save(any(Card.class));
//    }
//
//    @Test
//    void updateCardStatus_ShouldBlockCard() {
//        // Arrange
//        Long cardId = 1L;
//        Card card = Card.builder()
//                .id(cardId)
//                .status(CardStatus.ACTIVE)
//                .build();
//
//        Card blockedCard = Card.builder()
//                .id(cardId)
//                .status(CardStatus.BLOCKED)
//                .build();
//
//        CardDTO expectedDto = CardDTO.builder()
//                .id(cardId)
//                .status(CardStatus.BLOCKED)
//                .build();
//
//        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
//        when(cardRepository.save(card)).thenReturn(blockedCard);
//        when(cardMapper.toDTO(blockedCard)).thenReturn(expectedDto);
//
//        // Act
//        CardDTO result = cardService.updateCardStatus(cardId, CardStatus.BLOCKED);
//
//        // Assert
//        assertEquals(CardStatus.BLOCKED, result.getStatus());
//    }
//
//    @Test
//    void processBlockRequest_ShouldApproveRequest() {
//        // Arrange
//        Long requestId = 1L;
//        Card card = Card.builder()
//                .id(1L)
//                .status(CardStatus.ACTIVE)
//                .build();
//
//        CardBlockRequest request = CardBlockRequest.builder()
//                .id(requestId)
//                .card(card)
//                .status(RequestStatus.PENDING)
//                .build();
//
//        CardBlockRequestDTO requestDto = new CardBlockRequestDTO(
//                true,
//                "User reason",
//                "Admin approved",
//                LocalDateTime.now(),
//                LocalDateTime.now()
//        );
//
//        Card blockedCard = Card.builder()
//                .id(1L)
//                .status(CardStatus.BLOCKED)
//                .build();
//
//        CardDTO expectedDto = CardDTO.builder()
//                .id(1L)
//                .status(CardStatus.BLOCKED)
//                .build();
//
//        when(blockRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
//        when(cardRepository.save(card)).thenReturn(blockedCard);
//        when(cardMapper.toDTO(blockedCard)).thenReturn(expectedDto);
//
//        // Act
//        CardDTO result = cardService.processBlockRequest(requestId, requestDto);
//
//        // Assert
//        assertEquals(CardStatus.BLOCKED, result.getStatus());
//        assertEquals(RequestStatus.APPROVED, request.getStatus());
//        assertEquals("Admin approved", request.getAdminComment());
//    }
//
//    @Test
//    void unblockCard_ShouldSetExpired_WhenCardExpired() {
//        // Arrange
//        Long cardId = 1L;
//        Card card = Card.builder()
//                .id(cardId)
//                .status(CardStatus.BLOCKED)
//                .expiryDate(LocalDate.now().minusDays(1))
//                .build();
//
//        CardDTO expectedDto = CardDTO.builder()
//                .id(cardId)
//                .status(CardStatus.EXPIRED)
//                .build();
//
//        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
//        when(cardMapper.toDTO(card)).thenReturn(expectedDto);
//
//        // Act
//        CardDTO result = cardService.unblockCard(cardId);
//
//        // Assert
//        assertEquals(CardStatus.EXPIRED, result.getStatus());
//    }
//
//    @Test
//    void getAllCards_ShouldReturnPage() {
//        // Arrange
//        Pageable pageable = PageRequest.of(0, 10);
//        Card card = Card.builder().id(1L).build();
//        Page<Card> cardPage = new PageImpl<>(List.of(card));
//
//        CardDTO cardDto = CardDTO.builder().id(1L).build();
//
//        when(cardRepository.findAll(pageable)).thenReturn(cardPage);
//        when(cardMapper.toDTO(card)).thenReturn(cardDto);
//
//        // Act
//        Page<CardDTO> result = cardService.getAllCards(pageable);
//
//        // Assert
//        assertEquals(1, result.getContent().size());
//        assertEquals(1L, result.getContent().get(0).getId());
//    }
//}