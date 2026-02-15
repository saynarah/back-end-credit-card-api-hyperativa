package saynab.api_wallet_backend.controllers.DTOs;

public record LoginResponseDTO(
        String accessToken,
        java.time.Instant expiresIn) {
}
