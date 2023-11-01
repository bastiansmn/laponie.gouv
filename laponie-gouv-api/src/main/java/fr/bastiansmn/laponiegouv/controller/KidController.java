package fr.bastiansmn.laponiegouv.controller;

import fr.bastiansmn.laponiegouv.dto.KidWishCreationDto;
import fr.bastiansmn.laponiegouv.exception.FunctionalException;
import fr.bastiansmn.laponiegouv.model.Kid;
import fr.bastiansmn.laponiegouv.model.Wish;
import fr.bastiansmn.laponiegouv.service.KidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kid")
@RequiredArgsConstructor
public class KidController {

    private final KidService kidService;

    @PostMapping
    public ResponseEntity<Kid> createKid(String name) {
        return ResponseEntity.ok(kidService.createKid(name));
    }

    @PostMapping("/wish")
    public ResponseEntity<Wish> createKidWish(@RequestBody KidWishCreationDto kidWishCreationDto)
            throws FunctionalException {
        return ResponseEntity.ok(kidService.createKidWish(kidWishCreationDto));
    }

}
