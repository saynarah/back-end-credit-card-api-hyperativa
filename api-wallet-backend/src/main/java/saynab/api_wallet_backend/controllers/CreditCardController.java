package saynab.api_wallet_backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import saynab.api_wallet_backend.config.SecurityConfig;
import saynab.api_wallet_backend.controllers.DTOs.CreditCardResponseDTO;
import saynab.api_wallet_backend.controllers.DTOs.RequestSingleCardDTO;
import saynab.api_wallet_backend.services.CreditCardService;

import java.io.IOException;

@RestController
@RequestMapping("/cards")
@Tag(name="creditCard",description = "Controller to upload and to retrieve cards information")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class CreditCardController {

    private CreditCardService creditCardService;

    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @PostMapping("/single")
    @Operation(summary = "Add a single card to a user", description = "Method to create a single card")
    @ApiResponse(responseCode = "201",description = "Card created sucessfully")
    @ApiResponse(responseCode = "500",description = "Internal server error")
    public ResponseEntity<Void> addSingleCard(@RequestBody RequestSingleCardDTO dto,
                                              JwtAuthenticationToken token){

        creditCardService.addSingleCard(dto,token);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/{cardNumber}")
    @Operation(summary = "Retrieve a card", description = "Method to retrieve a card")
    @ApiResponse(responseCode = "200",description = "Card is available on the database.")
    @ApiResponse(responseCode = "400",description = "Card not found.")
    @ApiResponse(responseCode = "500",description = "Internal server error")
    public ResponseEntity<CreditCardResponseDTO> findCardById(@PathVariable("cardNumber") String cardNumber,
                                                              JwtAuthenticationToken token){
        var response = creditCardService.findCardById(cardNumber,token);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/batch")
    @Operation(summary = "Add a batch of cards to a user", description = "Method to create multiple cards")
    @ApiResponse(responseCode = "201",description = "Cards created sucessfully")
    @ApiResponse(responseCode = "500",description = "Internal server error")
    public ResponseEntity<Void> addBatchOfCards(@RequestParam("file") MultipartFile file,
                                                JwtAuthenticationToken token) throws IOException {

        creditCardService.processFile(file,token);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
