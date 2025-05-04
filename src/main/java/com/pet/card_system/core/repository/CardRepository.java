package com.pet.card_system.core.repository;

import com.pet.card_system.core.repository.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByUserId(Long userId);

    Optional<Card> findByIdAndUserId(Long cardId, Long userId);

}
