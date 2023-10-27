package fr.bastiansmn.laponiegouv.controller;

import fr.bastiansmn.laponiegouv.dto.FamilyCreationDto;
import fr.bastiansmn.laponiegouv.exception.FunctionalException;
import fr.bastiansmn.laponiegouv.model.Family;
import fr.bastiansmn.laponiegouv.model.User;
import fr.bastiansmn.laponiegouv.service.FamilyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/family")
public class FamilyController {

    private final FamilyService familyService;

    @GetMapping
    public ResponseEntity<Family> getFamily(@RequestParam("id") Long id) throws FunctionalException {
        return ResponseEntity.ok(familyService.getFamily(id));
    }

    @PostMapping
    public ResponseEntity<Family> createFamily(@RequestBody FamilyCreationDto familyCreationDto) {
        return ResponseEntity.ok(familyService.createFamily(familyCreationDto));
    }

    @PutMapping
    public ResponseEntity<Family> addUser(@RequestParam("id") Long id, @RequestBody User user)
            throws FunctionalException {
        return ResponseEntity.ok(familyService.addUserInFamily(id, user));
    }

    @DeleteMapping
    public ResponseEntity<Void> removeUser(@RequestParam("id") Long id, @RequestBody User user) throws FunctionalException {
        familyService.removeUser(id, user);
        return ResponseEntity.noContent().build();
    }

}
