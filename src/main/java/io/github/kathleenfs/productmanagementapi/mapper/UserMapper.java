package io.github.kathleenfs.productmanagementapi.mapper;

import io.github.kathleenfs.productmanagementapi.domain.entity.Role;
import io.github.kathleenfs.productmanagementapi.domain.entity.User;
import io.github.kathleenfs.productmanagementapi.dto.request.UserRequestDTO;
import io.github.kathleenfs.productmanagementapi.dto.response.UserResponseDTO;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User toEntity(UserRequestDTO request) {
        return new User(
                request.name(),
                request.email(),
                request.password()
        );
    }

    public UserResponseDTO toResponse(User user) {

        Set<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getActive(),
                roles
        );
    }
}