package saynab.api_wallet_backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import saynab.api_wallet_backend.config.SecurityConfig;
import saynab.api_wallet_backend.controllers.DTOs.LoginRequestDTO;
import saynab.api_wallet_backend.controllers.DTOs.LoginResponseDTO;
import saynab.api_wallet_backend.services.TokenService;

@RestController
@Tag(name="token",description = "Controller to generate Token")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class TokenController {

    private TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/auth")
    @Operation(summary = "Authenticate user", description = "Method to generate a token for other End points access")
    @ApiResponse(responseCode = "200",description = "Token created sucessfully")
    @ApiResponse(responseCode = "400",description = "User not found ")
    @ApiResponse(responseCode = "500",description = "Internal server error")
    public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody LoginRequestDTO loginRequestDTO){

        var response = tokenService.createToken(loginRequestDTO);

        return ResponseEntity.ok(response);
    }
}
