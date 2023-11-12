package fr.bastiansmn.laponiegouv.service;

import fr.bastiansmn.laponiegouv.dto.WishCreationDto;
import fr.bastiansmn.laponiegouv.exception.FunctionalException;
import fr.bastiansmn.laponiegouv.exception.FunctionalRule;
import fr.bastiansmn.laponiegouv.exception.TechnicalException;
import fr.bastiansmn.laponiegouv.model.User;
import fr.bastiansmn.laponiegouv.model.Wish;
import fr.bastiansmn.laponiegouv.repository.UserRepository;
import fr.bastiansmn.laponiegouv.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishService {

    private final UserService userService;
    private final MailService mailService;
    private final TemplateEngine templateEngine;
    private final WishRepository wishRepository;
    private final UserRepository userRepository;

    public Wish createWish(WishCreationDto wishCreationDto) throws FunctionalException {
        Wish wish = new Wish();
        wish.setLink(wishCreationDto.link());
        wish.setName(wishCreationDto.name());
        wish.setPrice(wishCreationDto.price());
        wish.setComment(wishCreationDto.comment());

        wish = wishRepository.save(wish);

        User user = userService.getByMail(wishCreationDto.email());
        user.getWishes().add(wish);

        userRepository.save(user);

        return wish;
    }

    public Wish markAsGifted(Long id, String gifter, String email) throws FunctionalException, TechnicalException {
        return markAs(id, true, gifter, email);
    }

    public Wish markAsNotGifted(Long id) throws FunctionalException, TechnicalException {
        return markAs(id, false, null, null);
    }

    public void deleteWish(Long id, Long userID) throws FunctionalException {
        Wish wish = wishRepository.findById(id)
                .orElseThrow(() -> new FunctionalException(
                        FunctionalRule.WISH_0001
                ));

        userRepository.findById(userID)
                .orElseThrow(() -> new FunctionalException(
                        FunctionalRule.USER_0001
                ))
                .getWishes()
                .remove(wish);

        wishRepository.delete(wish);
    }

    private Wish markAs(Long id, boolean gifted, String gifter, String email)
            throws FunctionalException, TechnicalException {
        Wish wish = wishRepository.findById(id)
                .orElseThrow(() -> new FunctionalException(
                        FunctionalRule.WISH_0001
                ));

        wish.setGifted(gifted);
        wish.setGifter(gifter);

        if (gifted) {
            // Send email
            final Context ctx = new Context();
            ctx.setVariable("wish", wish);
            ctx.setVariable("gifter", gifter);
            String htmlContent = templateEngine.process("gifted", ctx);

            mailService.sendMail(
                    "bastian.somon@gmail.com",
                    List.of(email),
                    "Laponie Gouv - Rappel pour votre cadeau",
                    htmlContent,
                    null
            );
        }

        return wishRepository.save(wish);
    }

    public Wish updateWish(WishCreationDto wishCreationDto, Long id) throws FunctionalException {
        Wish wish = wishRepository.findById(id)
                .orElseThrow(() -> new FunctionalException(
                        FunctionalRule.WISH_0001
                ));

        wish.setName(wishCreationDto.name());
        wish.setLink(wishCreationDto.link());
        wish.setPrice(wishCreationDto.price());
        wish.setComment(wishCreationDto.comment());

        return wishRepository.save(wish);
    }
}
