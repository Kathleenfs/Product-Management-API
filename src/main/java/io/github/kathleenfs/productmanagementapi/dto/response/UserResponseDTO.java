package io.github.kathleenfs.productmanagementapi.dto.response;

import java.util.Set;

public record UserResponseDTO(

        Long id,
        String name,
        String email,
        Boolean active,
        Set<String> roles

) {
}