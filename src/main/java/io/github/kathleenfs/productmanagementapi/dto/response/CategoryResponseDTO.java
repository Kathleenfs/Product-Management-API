package io.github.kathleenfs.productmanagementapi.dto.response;

import java.time.LocalDateTime;

public record CategoryResponseDTO(

    Long id,

    String name,

    String description,

    Boolean active,

    LocalDateTime createdAt,

    LocalDateTime updatedAt

) {
    }
