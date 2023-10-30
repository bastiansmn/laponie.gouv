package fr.bastiansmn.laponiegouv.controller;

import fr.bastiansmn.laponiegouv.dto.UserCreationDto;
import fr.bastiansmn.laponiegouv.exception.FunctionalException;
import fr.bastiansmn.laponiegouv.model.Family;
import fr.bastiansmn.laponiegouv.model.User;
import fr.bastiansmn.laponiegouv.model.Wish;
import fr.bastiansmn.laponiegouv.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<User> getByMail(@RequestParam String email) throws FunctionalException {
        email = URLDecoder.decode(email, StandardCharsets.UTF_8);
        return ResponseEntity.ok(userService.getByMail(email));
    }

    @GetMapping("/families")
    public ResponseEntity<List<Family>> getUsersFamilies(@RequestParam Long id) throws FunctionalException {
        return ResponseEntity.ok(userService.getUsersFamilies(id));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserCreationDto userCreationDto) {
        return ResponseEntity.ok(userService.createUser(userCreationDto));
    }

    @GetMapping("/wishes")
    public ResponseEntity<List<Wish>> getUserWishes(@RequestParam Long id) throws FunctionalException {
        return ResponseEntity.ok(userService.getUserWishes(id));
    }

}

