package fr.bastiansmn.laponiegouv.controller;

import fr.bastiansmn.laponiegouv.dto.UserCreationDto;
import fr.bastiansmn.laponiegouv.exception.FunctionalException;
import fr.bastiansmn.laponiegouv.model.User;
import fr.bastiansmn.laponiegouv.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserCreationDto userCreationDto) {
        return ResponseEntity.ok(userService.createUser(userCreationDto));
    }

    @GetMapping
    public ResponseEntity<User> getUserByUuid(@RequestParam Long id) throws FunctionalException {
        return ResponseEntity.ok(userService.getUserByUuid(id));
    }

}

