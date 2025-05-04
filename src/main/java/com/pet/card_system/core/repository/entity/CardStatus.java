package com.pet.card_system.core.repository.entity;

public enum CardStatus {
    /**
     * The card is active and can be used for transactions.
     */
    ACTIVE,

    /**
     * The card is blocked (by the administrator or by user request)
     */
    BLOCKED,

    /**
     * Card has expired
     */
    EXPIRED;

    /**
     * Checks if the card can be used for transactions
     */
    public boolean isOperational() {
        return this == ACTIVE;
    }
}