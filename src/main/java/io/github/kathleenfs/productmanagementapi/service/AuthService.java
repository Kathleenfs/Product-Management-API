package io.github.kathleenfs.productmanagementapi.service;

import io.github.kathleenfs.productmanagementapi.domain.entity.User;
import io.github.kathleenfs.productmanagementapi.dto.request.LoginRequestDTO;
import io.github.kathleenfs.productmanagementapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User authenticate(LoginRequestDTO request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid email or password")
                );

        boolean passwordMatches = passwordEncoder.matches(
                request.password(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        if (!user.getActive()) {
            throw new IllegalStateException("User is inactive");
        }

        return user;
    }
}