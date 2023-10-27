package fr.bastiansmn.laponiegouv.service;

import fr.bastiansmn.laponiegouv.dto.FamilyCreationDto;
import fr.bastiansmn.laponiegouv.exception.FunctionalException;
import fr.bastiansmn.laponiegouv.exception.FunctionalRule;
import fr.bastiansmn.laponiegouv.model.Family;
import fr.bastiansmn.laponiegouv.model.User;
import fr.bastiansmn.laponiegouv.repository.FamilyRepository;
import fr.bastiansmn.laponiegouv.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;

    public Family getFamily(Long id) throws FunctionalException {
        return familyRepository.findById(id).
        orElseThrow(() -> new FunctionalException(FunctionalRule.FAMILY_0001));
    }

    public Family createFamily(FamilyCreationDto familyCreationDto) {
        Family family = new Family();
        family.setName(familyCreationDto.name());
        family.setUsers(new LinkedList<>());
        return familyRepository.save(family);
    }

    public Family addUserInFamily(Long familyId, User user) throws FunctionalException {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new FunctionalException(FunctionalRule.FAMILY_0001));

        User savedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new FunctionalException(FunctionalRule.USER_0001));

        family.getUsers().add(savedUser);
        return familyRepository.save(family);
    }

    public void removeUser(Long familyId, User user) throws FunctionalException {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new FunctionalException(FunctionalRule.FAMILY_0001));

        boolean result = family.getUsers().remove(user);

        if (!result)
            throw new FunctionalException(FunctionalRule.FAMILY_0002);

        familyRepository.save(family);
    }
}
