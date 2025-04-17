package ies.carbox.api.RestAPI.service;

import java.util.ArrayList;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ies.carbox.api.RestAPI.dtos.LoginUserDto;
import ies.carbox.api.RestAPI.dtos.RegisterUserDto;
import ies.carbox.api.RestAPI.entity.Role;
import ies.carbox.api.RestAPI.entity.User;
import ies.carbox.api.RestAPI.repository.UserRepository;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final CacheService cacheService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
        UserRepository userRepository,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder,
        CacheService cacheService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cacheService = cacheService;
    }

    public User signup(RegisterUserDto input) {
        User user = new User();
        user.setName(input.getUsername());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setPhone(input.getPhone());
        user.setRole(Role.USER);
        if (input.getCarsList() != null)
            user.setCarsList(input.getCarsList());
        else
            user.setCarsList(new ArrayList<>());
        System.out.println(user);
        cacheService.saveUser(user);
        return userRepository.save(user);
    }

    public User update(RegisterUserDto input) {
        User user = new User();
        user.setName(input.getUsername());
        user.setEmail(input.getEmail());
        user.setPassword(input.getPassword());
        user.setPhone(input.getPhone());
        user.setRole(Role.USER);
        if (input.getCarsList() != null)
            user.setCarsList(input.getCarsList());
        else
            user.setCarsList(new ArrayList<>());
        System.out.println(user);
        cacheService.saveUser(user);
        return userRepository.save(user);
    }
    

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );
            
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow();

        cacheService.saveUser(user);
        return user;
    }
}
