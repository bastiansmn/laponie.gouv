package fr.bastiansmn.laponiegouv.dto;

import java.util.List;
import java.util.Map;
import java.util.Set;

public record WishDrawDto(
        List<String> excludedEmails,
        Map<String, Set<String>> incompatibleDraws
) { }
