package com.security.session.service;

import com.security.session.exception.UserAlreadyExistsException;
import com.security.session.model.AuthenticationRequest;
import com.security.session.model.AuthenticationResponse;
import com.security.session.model.RegisterRequest;
import com.security.session.model.entity.Role;
import com.security.session.model.entity.User;
import com.security.session.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository repository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        User user = repository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalStateException(
                        "Kullanıcı doğrulandı ancak veritabanında bulunamadı: " + request.username()
                ));
        String jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        if (repository.existsByUsername(request.username())) {
            throw new UserAlreadyExistsException(
                    "Bu kullanıcı adı zaten kullanılıyor: " + request.username()
            );
        }
        User user = new User(
                request.username(),
                passwordEncoder.encode(request.password()),
                Role.ROLE_USER
        );
        User savedUser = repository.save(user);
        String jwtToken = jwtService.generateToken(savedUser);
        return new AuthenticationResponse(jwtToken);
    }
}