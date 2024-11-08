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
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FamilyService {

    private final MailService mailService;
    private final UserService userService;
    private final KidService kidService;
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

        User user = userRepository.findByEmailIgnoreCase(familyCreationDto.email())
                .orElseThrow(() -> new FunctionalException(FunctionalRule.USER_0001));

        family.setUsers(List.of(user));
        return familyRepository.save(family);
    }

    public Family addUserInFamily(Long familyId, String userEmail) throws FunctionalException, TechnicalException {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new FunctionalException(FunctionalRule.FAMILY_0001));

        Optional<User> savedUser = userRepository.findByEmailIgnoreCase(userEmail);
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

    public Family addKidInFamily(Long familyId, String kidName) throws FunctionalException {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new FunctionalException(FunctionalRule.FAMILY_0001));

        family.getKids().add(kidService.createKid(kidName));

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

    public Map<?, ?> drawWish(Long id, List<String> excludedEmails, Map<String, Set<String>> incompatibleDraw, boolean genderFill) throws FunctionalException {
        Family family = familyRepository.findById(id)
                .orElseThrow(() -> new FunctionalException(FunctionalRule.FAMILY_0001));

        Map<User, Optional<User>> draw = family.getUsers()
                .stream()
                .filter(user -> !excludedEmails.contains(user.getEmail()))
                .collect(Collectors.toMap(user -> user, user -> Optional.empty()));

        family.getDraw()
                .forEach((k, v) -> {
                    Set<String> emails = incompatibleDraw.getOrDefault(k, new HashSet<>());
                    emails.add(v);
                    incompatibleDraw.put(k, emails);
                });

        // Tant que qqn n'a pas de cadeau à donner
        while (draw.values().stream().anyMatch(Optional::isEmpty)) {
            // On cherche cette personne
            User giver = draw.keySet().stream().filter(k -> draw.get(k).isEmpty()).findAny()
                    .orElseThrow();

            // Randomly find a receiver that is not receiver already.
            List<User> allUnlinkedUsers = draw.keySet().stream()
                    .filter(k -> !draw.values()
                            .stream()
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .toList()
                            .contains(k))
                    .filter(k -> !k.equals(giver))
                    .filter(k -> !genderFill || k.getSexe().equals(giver.getSexe()))
                    .toList();

            List<User> compatibleUnlinkedUsers = allUnlinkedUsers
                    .stream()
                    .filter(k -> {
                        Set<String> incompatibleReceiver = incompatibleDraw.getOrDefault(giver.getEmail(), null);
                        if (incompatibleReceiver != null) {
                            return !incompatibleReceiver.contains(k.getEmail()); // Exclure les emails incompatibles.
                        }
                        return true; // Il n'y a pas d'incompatibilité pour cet utilisateur
                    })
                    .toList();

            // Si allUnlinkedUsers est vide, c'est que le tirage est bloqué.
            // Il faut supprimer un tirage où receiver!=current && receiver n'est pas incompatible
            Random random = new Random();
            if (compatibleUnlinkedUsers.isEmpty()) {
                for (Map.Entry<User, Optional<User>> entry : draw.entrySet()) {
                    Optional<User> receiver = entry.getValue();
                    Set<String> incompatibleReceiver = incompatibleDraw.getOrDefault(giver.getEmail(), Set.of());
                    if (
                            receiver.isPresent()
                        && !receiver.get().equals(giver)
                        && !incompatibleReceiver.contains(receiver.get().getEmail())
                    ) {
                        draw.remove(entry.getKey());
                        break;
                    }
                }

                continue;
            }

            int randomIndex = random.nextInt(compatibleUnlinkedUsers.size());
            User receiver = compatibleUnlinkedUsers.get(randomIndex);

            draw.put(giver, Optional.of(receiver));
        }

        AtomicBoolean drawSuccessfull = new AtomicBoolean(true);
        draw.forEach((key, value) -> {
            final Context ctx = new Context();
            ctx.setVariable("giver", key);
            ctx.setVariable("receiver", value.get());
            ctx.setVariable("familyLink",
                    applicationProperties.getUrl()
                        + "/home"
                        + "?email=" + URLEncoder.encode(key.getEmail(), StandardCharsets.UTF_8)
                        + "&redirect=" + URLEncoder.encode("/family/" + id, StandardCharsets.UTF_8)
            );
            String htmlContent = templateEngine.process("draw", ctx);

            try {
                mailService.sendMail(
                        "laponie-gouv.bastian-somon.fr",
                        List.of(key.getEmail()),
                        "Tirage au sort de Noël",
                        htmlContent,
                        null
                );
            } catch (TechnicalException e) {
                drawSuccessfull.set(false);
            }
        });

        if (!drawSuccessfull.get())
            throw new FunctionalException(FunctionalRule.FAMILY_0004);

        // Get a map of giver.getMail() -> receiver.getMail()
        Map<String, String> emailMap = draw.entrySet().stream()
                .collect(Collectors.toMap(userOptionalEntry -> userOptionalEntry.getKey().getEmail(), entry -> entry.getValue().get().getEmail()));

        family.setDraw(emailMap);
        familyRepository.save(family);

        return emailMap;
    }
}
