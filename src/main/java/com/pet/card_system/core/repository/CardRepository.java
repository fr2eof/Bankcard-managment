package com.pet.card_system.core.repository;

import com.pet.card_system.core.repository.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;


public interface CardRepository extends JpaRepository<Card, Long>, JpaSpecificationExecutor<Card> {
    Page<Card> findByUserId(Long userId, Pageable pageable);

    @NonNull
    @EntityGraph(attributePaths = {"user"})
    Page<Card> findAll(@Nullable Specification<Card> spec, @NonNull Pageable pageable);

    List<Card> findAllByUserId(Long userId);
}
