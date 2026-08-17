package service;

import io.github.kathleenfs.productmanagementapi.domain.entity.User;
import io.github.kathleenfs.productmanagementapi.dto.request.LoginRequestDTO;
import io.github.kathleenfs.productmanagementapi.dto.response.LoginResponseDTO;
import io.github.kathleenfs.productmanagementapi.repository.UserRepository;
import io.github.kathleenfs.productmanagementapi.service.AuthService;
import io.github.kathleenfs.productmanagementapi.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(
                userRepository,
                passwordEncoder,
                jwtService
        );
    }

    @Test
    void shouldLoginSuccessfully() {

        LoginRequestDTO request = new LoginRequestDTO(
                "kathleen@email.com",
                "Senha123"
        );

        User user = new User(
                "Kathleen",
                "kathleen@email.com",
                "hashed-password"
        );

        user.setActive(true);

        when(userRepository.findByEmail(request.email()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "Senha123",
                "hashed-password"
        )).thenReturn(true);

        when(jwtService.generateToken(user))
                .thenReturn("fake-jwt-token");

        LoginResponseDTO result =
                authService.login(request);

        assertEquals("fake-jwt-token", result.token());
        assertEquals("Bearer", result.type());

        verify(userRepository)
                .findByEmail("kathleen@email.com");

        verify(passwordEncoder)
                .matches("Senha123", "hashed-password");

        verify(jwtService)
                .generateToken(user);
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsInvalid() {

        LoginRequestDTO request = new LoginRequestDTO(
                "kathleen@email.com",
                "SenhaErrada"
        );

        User user = new User(
                "Kathleen",
                "kathleen@email.com",
                "hashed-password"
        );

        user.setActive(true);

        when(userRepository.findByEmail(request.email()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "SenhaErrada",
                "hashed-password"
        )).thenReturn(false);

        assertThrows(
                IllegalArgumentException.class,
                () -> authService.login(request)
        );

        verify(userRepository)
                .findByEmail("kathleen@email.com");

        verify(passwordEncoder)
                .matches("SenhaErrada", "hashed-password");

        verify(jwtService, never())
                .generateToken(any());
    }

    @Test
    void shouldThrowExceptionWhenEmailDoesNotExist() {

        LoginRequestDTO request = new LoginRequestDTO(
                "naoexiste@email.com",
                "Senha123"
        );

        when(userRepository.findByEmail(request.email()))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> authService.login(request)
        );

        verify(userRepository)
                .findByEmail("naoexiste@email.com");

        verify(passwordEncoder, never())
                .matches(anyString(), anyString());

        verify(jwtService, never())
                .generateToken(any());
    }

    @Test
    void shouldThrowExceptionWhenUserIsInactive() {

        LoginRequestDTO request = new LoginRequestDTO(
                "kathleen@email.com",
                "Senha123"
        );

        User user = new User(
                "Kathleen",
                "kathleen@email.com",
                "hashed-password"
        );

        user.setActive(false);

        when(userRepository.findByEmail(request.email()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "Senha123",
                "hashed-password"
        )).thenReturn(true);

        assertThrows(
                IllegalStateException.class,
                () -> authService.login(request)
        );

        verify(userRepository)
                .findByEmail("kathleen@email.com");

        verify(passwordEncoder)
                .matches("Senha123", "hashed-password");

        verify(jwtService, never())
                .generateToken(any());
    }
}
