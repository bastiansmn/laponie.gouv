package fr.bastiansmn.laponiegouv.service;

import fr.bastiansmn.laponiegouv.configuration.properties.ApplicationProperties;
import fr.bastiansmn.laponiegouv.dto.FamilyCreationDto;
import fr.bastiansmn.laponiegouv.dto.UserCreationDto;
import fr.bastiansmn.laponiegouv.exception.FunctionalException;
import fr.bastiansmn.laponiegouv.exception.FunctionalRule;
import fr.bastiansmn.laponiegouv.exception.TechnicalException;
import fr.bastiansmn.laponiegouv.model.Family;
import fr.bastiansmn.laponiegouv.model.User;
import fr.bastiansmn.laponiegouv.repository.FamilyRepository;
import fr.bastiansmn.laponiegouv.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FamilyService {

    private final MailService mailService;
    private final UserService userService;
    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;
    private final TemplateEngine templateEngine;

    private final ApplicationProperties applicationProperties;

    public Family getFamily(Long id) throws FunctionalException {
        return familyRepository.findById(id)
                .orElseThrow(() -> new FunctionalException(FunctionalRule.FAMILY_0001));
    }

    public Family createFamily(FamilyCreationDto familyCreationDto) throws FunctionalException {
        Family family = new Family();
        family.setName(familyCreationDto.name());

        User user = userRepository.findByEmail(familyCreationDto.email())
                .orElseThrow(() -> new FunctionalException(FunctionalRule.USER_0001));

        family.setUsers(List.of(user));
        return familyRepository.save(family);
    }

    public Family addUserInFamily(Long familyId, String userEmail) throws FunctionalException, TechnicalException {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new FunctionalException(FunctionalRule.FAMILY_0001));

        Optional<User> savedUser = userRepository.findByEmail(userEmail);
        User user;

        if (savedUser.isEmpty()) {
            // Send invitation to user and create it
            final Context ctx = new Context();
            ctx.setVariable(
                "authLink", applicationProperties.getUrl() + "/home" +
                    "?email=" + URLEncoder.encode(userEmail, StandardCharsets.UTF_8) +
                    "&redirect=" + URLEncoder.encode("/family/" + familyId, StandardCharsets.UTF_8)
            );
            String htmlContent = templateEngine.process("invitation", ctx);

            mailService.sendMail(
                    "laponie-gouv.bastian-somon.fr",
                    List.of(userEmail),
                    "Invitation à rejoindre un groupe famille sur laponie-gouv.bastian-somon.fr",
                    htmlContent,
                    null
            );

            user = userService.createUser(new UserCreationDto(userEmail, userEmail.split("@")[0]));
        } else {
            user = savedUser.get();
        }

        if (family.getUsers().contains(user))
            throw new FunctionalException(FunctionalRule.FAMILY_0003);

        family.getUsers().add(user);
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
