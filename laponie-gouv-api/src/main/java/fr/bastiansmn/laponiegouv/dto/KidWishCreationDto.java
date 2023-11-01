package fr.bastiansmn.laponiegouv.dto;

import jakarta.annotation.Nullable;

public record KidWishCreationDto(
    Long kidID,
    String link,
    @Nullable String name,
    @Nullable Integer price,
    @Nullable String comment
) { }
