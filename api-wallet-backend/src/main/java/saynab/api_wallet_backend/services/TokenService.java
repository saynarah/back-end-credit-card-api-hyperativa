package saynab.api_wallet_backend.services;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import saynab.api_wallet_backend.controllers.DTOs.LoginRequestDTO;
import saynab.api_wallet_backend.controllers.DTOs.LoginResponseDTO;
import saynab.api_wallet_backend.repositories.UserRepository;

import java.time.Instant;

@Service
public class TokenService {

    private UserRepository userRepository;
    private final JwtEncoder jwtEncoder;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public TokenService(JwtEncoder jwtEncoder,
                        BCryptPasswordEncoder bCryptPasswordEncoder,
                        UserRepository userRepository) {
        this.jwtEncoder = jwtEncoder;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    public LoginResponseDTO createToken(LoginRequestDTO loginRequestDTO){

        var user = userRepository.findByUsername(loginRequestDTO.username());

        if(user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        else if(!user.get().isLoginCorrect(loginRequestDTO,bCryptPasswordEncoder)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        var now = Instant.now();
        var expiresIn = 300L;

        var claims = JwtClaimsSet.builder()
                .issuer("api-wallet-backend")
                .subject(user.get().getId().toString())
                .expiresAt(now.plusSeconds(expiresIn))
                .issuedAt(now)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponseDTO(jwtValue,claims.getExpiresAt());

    }
}
