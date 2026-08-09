package io.github.kathleenfs.productmanagementapi.service;

import io.github.kathleenfs.productmanagementapi.domain.entity.User;
import io.github.kathleenfs.productmanagementapi.dto.request.LoginRequestDTO;
import io.github.kathleenfs.productmanagementapi.dto.response.LoginResponseDTO;
import io.github.kathleenfs.productmanagementapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponseDTO login(LoginRequestDTO request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid email or password")
                );

        if (!passwordEncoder.matches(
                request.password(),
                user.getPassword()
        )) {
            throw new IllegalArgumentException(
                    "Invalid email or password"
            );
        }

        if (!user.getActive()) {
            throw new IllegalStateException("User is inactive");
        }

        String token = jwtService.generateToken(user);

        return new LoginResponseDTO(token, "Bearer");
    }
}