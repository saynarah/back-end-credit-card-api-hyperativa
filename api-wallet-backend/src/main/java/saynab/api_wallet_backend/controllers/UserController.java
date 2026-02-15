package saynab.api_wallet_backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import saynab.api_wallet_backend.config.SecurityConfig;
import saynab.api_wallet_backend.controllers.DTOs.CreateUserDTO;
import saynab.api_wallet_backend.services.UserService;

@RestController
@RequestMapping("/users")
@Tag(name="user",description = "Controller to create the user")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    @PostMapping
    @Operation(summary = "Create and save data from a new user", description = "Method to save users' data")
    @ApiResponse(responseCode = "201",description = "User created sucessfully")
    @ApiResponse(responseCode = "400",description = "This username is already saved")
    @ApiResponse(responseCode = "500",description = "Internal server error")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserDTO dto){

        userService.createUser(dto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
