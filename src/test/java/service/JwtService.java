package service;

import io.github.kathleenfs.productmanagementapi.domain.entity.User;
import io.github.kathleenfs.productmanagementapi.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        ReflectionTestUtils.setField(
                jwtService,
                "jwtSecret",
                "12345678901234567890123456789012"
        );

        ReflectionTestUtils.setField(
                jwtService,
                "jwtExpiration",
                3600000L
        );
    }

    @Test
    void shouldGenerateTokenSuccessfully() {

        User user = new User(
                "Kathleen",
                "kathleen@email.com",
                "hashed-password"
        );

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void shouldExtractEmailFromToken() {

        User user = new User(
                "Kathleen",
                "kathleen@email.com",
                "hashed-password"
        );

        String token = jwtService.generateToken(user);

        String email = jwtService.extractEmail(token);

        assertEquals(
                "kathleen@email.com",
                email
        );
    }

    @Test
    void shouldValidateTokenSuccessfully() {

        User user = new User(
                "Kathleen",
                "kathleen@email.com",
                "hashed-password"
        );

        String token = jwtService.generateToken(user);

        UserDetails userDetails =
                org.springframework.security.core.userdetails.User
                        .withUsername("kathleen@email.com")
                        .password("hashed-password")
                        .authorities("ROLE_USER")
                        .build();

        boolean valid =
                jwtService.isTokenValid(token, userDetails);

        assertTrue(valid);
    }

    @Test
    void shouldReturnFalseWhenTokenBelongsToAnotherUser() {

        User user = new User(
                "Kathleen",
                "kathleen@email.com",
                "hashed-password"
        );

        String token = jwtService.generateToken(user);

        UserDetails anotherUser =
                org.springframework.security.core.userdetails.User
                        .withUsername("outro@email.com")
                        .password("hashed-password")
                        .authorities("ROLE_USER")
                        .build();

        boolean valid =
                jwtService.isTokenValid(token, anotherUser);

        assertFalse(valid);
    }

    @Test
    void shouldRejectExpiredToken() {

        ReflectionTestUtils.setField(
                jwtService,
                "jwtExpiration",
                -1000L
        );

        User user = new User(
                "Kathleen",
                "kathleen@email.com",
                "hashed-password"
        );

        String token = jwtService.generateToken(user);

        UserDetails userDetails =
                org.springframework.security.core.userdetails.User
                        .withUsername("kathleen@email.com")
                        .password("hashed-password")
                        .authorities("ROLE_USER")
                        .build();
        assertThrows(
                ExpiredJwtException.class,
                () -> jwtService.isTokenValid(token, userDetails)
        );
    }
}