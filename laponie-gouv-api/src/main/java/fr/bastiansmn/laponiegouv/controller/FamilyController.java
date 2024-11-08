package fr.bastiansmn.laponiegouv.controller;

import fr.bastiansmn.laponiegouv.dto.FamilyCreationDto;
import fr.bastiansmn.laponiegouv.dto.WishDrawDto;
import fr.bastiansmn.laponiegouv.exception.FunctionalException;
import fr.bastiansmn.laponiegouv.exception.TechnicalException;
import fr.bastiansmn.laponiegouv.model.Family;
import fr.bastiansmn.laponiegouv.model.User;
import fr.bastiansmn.laponiegouv.service.FamilyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/family")
public class FamilyController {

    private final FamilyService familyService;

    @GetMapping
    public ResponseEntity<Family> getFamily(@RequestParam Long id) throws FunctionalException {
        return ResponseEntity.ok(familyService.getFamily(id));
    }

    @PostMapping
    public ResponseEntity<Family> createFamily(@RequestBody FamilyCreationDto familyCreationDto)
            throws FunctionalException {
        return ResponseEntity.ok(familyService.createFamily(familyCreationDto));
    }

    @PutMapping
    public ResponseEntity<Family> addUser(@RequestParam Long id, @RequestParam String email)
            throws FunctionalException, TechnicalException {
        email = URLDecoder.decode(email, StandardCharsets.UTF_8);
        return ResponseEntity.ok(familyService.addUserInFamily(id, email));
    }

    @PutMapping("/kid")
    public ResponseEntity<Family> addKid(@RequestParam Long id, @RequestParam String name)
            throws FunctionalException {
        return ResponseEntity.ok(familyService.addKidInFamily(id, name));
    }

    @DeleteMapping
    public ResponseEntity<Void> removeUser(@RequestParam Long id, @RequestBody User user) throws FunctionalException {
        familyService.removeUser(id, user);
        return ResponseEntity.noContent().build();
    }

    /**
     * Lancer un tirage au sort et avertir les participants de ce tirage.
     * @param id            ID de la famille où réaliser le tirage
     * @param genderFill    True=les hommes (resp. femmes) ne tirent que des hommes (femmes)
     * @param wishDrawDto   Objet contenant : la liste des personnes exclues du tirage + les paires non compatibles
     * @return Le tirage associé
     * @throws FunctionalException  Si le tirage n'a pas pu être réalisé
     */
    @PostMapping("/wish-draw")
    public ResponseEntity<Map<?, ?>> drawWish(
            @RequestParam Long id,
            @RequestParam(required = false, defaultValue = "false") boolean genderFill,
            @RequestBody(required = false) WishDrawDto wishDrawDto
    ) throws FunctionalException {
        List<String> excludedEmails;
        if (wishDrawDto.excludedEmails() == null) {
            excludedEmails = List.of();
        } else {
            excludedEmails = wishDrawDto.excludedEmails();
        }
        Map<String, Set<String>> incompatibleDraw;
        if (wishDrawDto.incompatibleDraws() == null) {
            incompatibleDraw = new HashMap<>();
        } else {
            incompatibleDraw = wishDrawDto.incompatibleDraws();
        }

        return ResponseEntity.ok(
                familyService.drawWish(id, excludedEmails, incompatibleDraw, genderFill)
        );
    }

}
