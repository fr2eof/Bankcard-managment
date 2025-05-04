package com.pet.card_system.core.mapper;

import com.pet.card_system.core.dto.CardDTO;
import com.pet.card_system.core.repository.entity.Card;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Component
public class CardMapper {

    public CardDTO toDTO(Card card) {
        if (card == null) {
            return null;
        }

        return new CardDTO(
                card.getId(),
                card.getNumberMasked(),
                card.getHolderName(),
                formatExpiryDate(card.getExpiryDate()),
                card.getStatus(),
                card.getBalance()
        );
    }

    public Card toEntity(CardDTO dto) {
        if (dto == null) {
            return null;
        }

        Card card = new Card();
        card.setId(dto.getId());
        card.setNumberMasked(dto.getNumberMasked());
        card.setHolderName(dto.getNumberMasked());
        card.setExpiryDate(parseExpiryDate(dto.getFormattedExpiryDate()));
        card.setStatus(dto.getStatus());
        card.setBalance(dto.getBalance());

        return card;
    }

    private String formatExpiryDate(LocalDate date) {
        return date != null
                ? DateTimeFormatter.ofPattern("MM/yy").format(date)
                : null;
    }

    private LocalDate parseExpiryDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        YearMonth yearMonth = YearMonth.parse(dateStr,
                DateTimeFormatter.ofPattern("MM/yy"));
        return yearMonth.atEndOfMonth();
    }
}
