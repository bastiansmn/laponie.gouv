package fr.bastiansmn.laponiegouv.service;

import fr.bastiansmn.laponiegouv.dto.KidWishCreationDto;
import fr.bastiansmn.laponiegouv.exception.FunctionalException;
import fr.bastiansmn.laponiegouv.exception.FunctionalRule;
import fr.bastiansmn.laponiegouv.model.Kid;
import fr.bastiansmn.laponiegouv.model.Wish;
import fr.bastiansmn.laponiegouv.repository.KidRepository;
import fr.bastiansmn.laponiegouv.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KidService {

    private final KidRepository kidRepository;
    private final WishRepository wishRepository;

    public Kid createKid(String name) {
        Kid kid = new Kid();
        kid.setName(name);
        kid.setWishes(List.of());

        return kidRepository.save(kid);
    }

    public Wish createKidWish(KidWishCreationDto wishCreationDto) throws FunctionalException {
        Wish wish = new Wish();
        wish.setLink(wishCreationDto.link());
        wish.setName(wishCreationDto.name());
        wish.setPrice(wishCreationDto.price());
        wish.setComment(wishCreationDto.comment());

        wish = wishRepository.save(wish);

        Kid kid = kidRepository.findById(wishCreationDto.kidID())
                .orElseThrow(() -> new FunctionalException(
                        FunctionalRule.KID_0001
                ));
        kid.getWishes().add(wish);

        kidRepository.save(kid);

        return wish;
    }
}
