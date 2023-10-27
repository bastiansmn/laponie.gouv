package fr.bastiansmn.laponiegouv.service;

import fr.bastiansmn.laponiegouv.dto.UserCreationDto;
import fr.bastiansmn.laponiegouv.exception.FunctionalException;
import fr.bastiansmn.laponiegouv.exception.FunctionalRule;
import fr.bastiansmn.laponiegouv.model.User;
import fr.bastiansmn.laponiegouv.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(UserCreationDto userCreationDto) {
        User user = new User();
        user.setName(userCreationDto.name());
        user.setEmail(userCreationDto.email());

        return userRepository.save(user);
    }

    public User getUserByUuid(Long id) throws FunctionalException {
        return userRepository.findById(id)
                .orElseThrow(() -> new FunctionalException(FunctionalRule.USER_0001));
    }

}
