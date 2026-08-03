package io.github.kathleenfs.productmanagementapi.service;

import io.github.kathleenfs.productmanagementapi.domain.entity.Role;
import io.github.kathleenfs.productmanagementapi.domain.entity.User;
import io.github.kathleenfs.productmanagementapi.dto.request.UserRequestDTO;
import io.github.kathleenfs.productmanagementapi.dto.response.UserResponseDTO;
import io.github.kathleenfs.productmanagementapi.mapper.UserMapper;
import io.github.kathleenfs.productmanagementapi.repository.RoleRepository;
import io.github.kathleenfs.productmanagementapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO create(UserRequestDTO request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already registered");
        }

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() ->
                        new IllegalStateException("Default role not found")
                );

        User user = userMapper.toEntity(request);

        user.setPassword(
                passwordEncoder.encode(request.password())
        );

        user.addRole(role);

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }
}