package fr.bastiansmn.laponiegouv.repository;

import fr.bastiansmn.laponiegouv.model.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {

    @Modifying
    @Query("UPDATE Wish w SET w.gifted = true WHERE w.id = ?1")
    void markAsGifted(Long w);

}
