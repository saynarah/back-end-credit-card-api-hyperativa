package saynab.api_wallet_backend.services;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import saynab.api_wallet_backend.controllers.DTOs.CreateUserDTO;
import saynab.api_wallet_backend.entities.User;
import saynab.api_wallet_backend.repositories.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Void createUser(CreateUserDTO createUserDTO){

        var userFromDB = userRepository.findByUsername(createUserDTO.username());

        if(userFromDB.isPresent()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var user = new User();
        user.setUsername(createUserDTO.username());
        user.setPassword(passwordEncoder.encode(createUserDTO.password()));
        userRepository.save(user);

        return null;
    }
}
