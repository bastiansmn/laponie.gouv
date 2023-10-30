package fr.bastiansmn.laponiegouv.service;

import fr.bastiansmn.laponiegouv.dto.WishCreationDto;
import fr.bastiansmn.laponiegouv.exception.FunctionalException;
import fr.bastiansmn.laponiegouv.exception.FunctionalRule;
import fr.bastiansmn.laponiegouv.model.User;
import fr.bastiansmn.laponiegouv.model.Wish;
import fr.bastiansmn.laponiegouv.repository.UserRepository;
import fr.bastiansmn.laponiegouv.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishService {

    private final UserService userService;
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

    public Wish markAsGifted(Long id) throws FunctionalException {
        return markAs(id, true);
    }

    public Wish markAsNotGifted(Long id) throws FunctionalException {
        return markAs(id, false);
    }

    public void deleteWish(Long id) {
        wishRepository.deleteById(id);
    }

    private Wish markAs(Long id, boolean gifted) throws FunctionalException {
        Wish wish = wishRepository.findById(id)
                .orElseThrow(() -> new FunctionalException(
                        FunctionalRule.WISH_0001
                ));

        wish.setGifted(gifted);
        return wishRepository.save(wish);
    }

}
