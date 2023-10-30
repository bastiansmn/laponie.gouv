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
    public ResponseEntity<Wish> markAsGifted(@RequestParam Long id) throws FunctionalException {
        return ResponseEntity.ok(wishService.markAsGifted(id));
    }

    @PatchMapping("/not-gifted")
    public ResponseEntity<Wish> markAsNotGifted(@RequestParam Long id) throws FunctionalException {
        return ResponseEntity.ok(wishService.markAsNotGifted(id));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteWish(Long id) {
        wishService.deleteWish(id);
        return ResponseEntity.noContent().build();
    }

}
