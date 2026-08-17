package service;

import io.github.kathleenfs.productmanagementapi.domain.entity.Role;
import io.github.kathleenfs.productmanagementapi.domain.entity.User;
import io.github.kathleenfs.productmanagementapi.dto.request.UserRequestDTO;
import io.github.kathleenfs.productmanagementapi.dto.response.UserResponseDTO;
import io.github.kathleenfs.productmanagementapi.mapper.UserMapper;
import io.github.kathleenfs.productmanagementapi.repository.RoleRepository;
import io.github.kathleenfs.productmanagementapi.repository.UserRepository;
import io.github.kathleenfs.productmanagementapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(
                userRepository,
                roleRepository,
                userMapper,
                passwordEncoder
        );
    }

    @Test
    void shouldCreateUserSuccessfully() {

        UserRequestDTO request = new UserRequestDTO(
                "Kathleen",
                "kathleen@email.com",
                "Senha123"
        );

        Role role = new Role("ROLE_USER");

        User user = new User(
                "Kathleen",
                "kathleen@email.com",
                "Senha123"
        );

        UserResponseDTO response = new UserResponseDTO(
                1L,
                "Kathleen",
                "kathleen@email.com",
                true,
                Set.of("ROLE_USER")
        );

        when(userRepository.existsByEmail(request.email()))
                .thenReturn(false);

        when(roleRepository.findByName("ROLE_USER"))
                .thenReturn(Optional.of(role));

        when(userMapper.toEntity(request))
                .thenReturn(user);

        when(passwordEncoder.encode(request.password()))
                .thenReturn("hashed-password");

        when(userRepository.save(user))
                .thenReturn(user);

        when(userMapper.toResponse(user))
                .thenReturn(response);

        UserResponseDTO result = userService.create(request);

        assertEquals("Kathleen", result.name());
        assertEquals("kathleen@email.com", result.email());

        verify(passwordEncoder).encode("Senha123");
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        UserRequestDTO request = new UserRequestDTO(
                "Kathleen",
                "kathleen@email.com",
                "Senha123"
        );

        when(userRepository.existsByEmail(request.email()))
                .thenReturn(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.create(request)
        );

        verify(userRepository)
                .existsByEmail("kathleen@email.com");

        verify(roleRepository, never())
                .findByName(anyString());

        verify(passwordEncoder, never())
                .encode(anyString());

        verify(userRepository, never())
                .save(any());
    }

    @Test
    void shouldThrowExceptionWhenDefaultRoleDoesNotExist() {

        UserRequestDTO request = new UserRequestDTO(
                "Kathleen",
                "kathleen@email.com",
                "Senha123"
        );

        when(userRepository.existsByEmail(request.email()))
                .thenReturn(false);

        when(roleRepository.findByName("ROLE_USER"))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalStateException.class,
                () -> userService.create(request)
        );

        verify(userRepository)
                .existsByEmail("kathleen@email.com");

        verify(roleRepository)
                .findByName("ROLE_USER");

        verify(passwordEncoder, never())
                .encode(anyString());

        verify(userRepository, never())
                .save(any());
    }
}