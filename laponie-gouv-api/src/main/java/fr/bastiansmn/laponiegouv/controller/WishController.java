package fr.bastiansmn.laponiegouv.controller;

import fr.bastiansmn.laponiegouv.dto.WishCreationDto;
import fr.bastiansmn.laponiegouv.exception.FunctionalException;
import fr.bastiansmn.laponiegouv.model.Wish;
import fr.bastiansmn.laponiegouv.service.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wish")
@RequiredArgsConstructor
public class WishController {

    private final WishService wishService;

    @PostMapping
    public ResponseEntity<Wish> createWish(@RequestBody WishCreationDto wishCreationDto) throws FunctionalException {
        return ResponseEntity.ok(wishService.createWish(wishCreationDto));
    }

    @PatchMapping("/gifted")
    public ResponseEntity<Wish> markAsGifted(@RequestParam Long id, @RequestParam String gifter) throws FunctionalException {
        return ResponseEntity.ok(wishService.markAsGifted(id, gifter));
    }

    @PatchMapping("/not-gifted")
    public ResponseEntity<Wish> markAsNotGifted(@RequestParam Long id) throws FunctionalException {
        return ResponseEntity.ok(wishService.markAsNotGifted(id));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteWish(@RequestParam Long id, @RequestParam Long userID)
            throws FunctionalException {
        wishService.deleteWish(id, userID);
        return ResponseEntity.noContent().build();
    }

}