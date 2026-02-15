package saynab.api_wallet_backend.controllers.DTOs;

import saynab.api_wallet_backend.entities.CreditCard;

import java.util.UUID;

public record CreditCardResponseDTO(UUID id) {

    public static CreditCardResponseDTO fromCreditCard(CreditCard creditCard){
        return new CreditCardResponseDTO(creditCard.getId());
    }
}
