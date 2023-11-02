package fr.bastiansmn.laponiegouv.service;

import fr.bastiansmn.laponiegouv.dto.UserCreationDto;
import fr.bastiansmn.laponiegouv.exception.FunctionalException;
import fr.bastiansmn.laponiegouv.exception.FunctionalRule;
import fr.bastiansmn.laponiegouv.model.Family;
import fr.bastiansmn.laponiegouv.model.User;
import fr.bastiansmn.laponiegouv.model.Wish;
import fr.bastiansmn.laponiegouv.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(UserCreationDto userCreationDto) throws FunctionalException {
        if (userRepository.findByEmail(userCreationDto.email()).isPresent()) {
            throw new FunctionalException(FunctionalRule.USER_0002);
        }

        User user = new User();
        user.setName(userCreationDto.name());
        user.setEmail(userCreationDto.email());
        user.setWishes(List.of());

        return userRepository.save(user);
    }

    public List<Wish> getUserWishes(Long id) throws FunctionalException {
        return userRepository.findById(id)
                .orElseThrow(() -> new FunctionalException(FunctionalRule.USER_0001))
                .getWishes();
    }

    public User getByMail(String mail) throws FunctionalException {
        return userRepository.findByEmail(mail)
                .orElseThrow(() -> new FunctionalException(FunctionalRule.USER_0001));
    }

    public List<Family> getUsersFamilies(Long id) throws FunctionalException {
        return userRepository.findById(id)
                .orElseThrow(() -> new FunctionalException(FunctionalRule.USER_0001))
                .getFamily();
    }

    public List<User> searchUser(String email) {
        return userRepository.findByEmailContaining(email);
    }
}
