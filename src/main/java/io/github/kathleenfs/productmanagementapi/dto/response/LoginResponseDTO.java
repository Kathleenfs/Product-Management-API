package io.github.kathleenfs.productmanagementapi.dto.response;


public record LoginResponseDTO(
        String token,
        String type
) {
}