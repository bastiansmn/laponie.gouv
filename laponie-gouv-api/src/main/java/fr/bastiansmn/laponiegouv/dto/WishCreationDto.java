package fr.bastiansmn.laponiegouv.dto;

import jakarta.annotation.Nullable;

public record WishCreationDto(
    String email,
    String link,
    @Nullable String name,
    @Nullable Integer price,
    @Nullable String comment
) { }
