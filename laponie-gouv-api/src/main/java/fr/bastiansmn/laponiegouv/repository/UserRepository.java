package fr.bastiansmn.laponiegouv.repository;

import fr.bastiansmn.laponiegouv.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String mail);
}
