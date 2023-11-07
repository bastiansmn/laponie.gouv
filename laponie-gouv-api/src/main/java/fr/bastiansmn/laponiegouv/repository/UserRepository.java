package fr.bastiansmn.laponiegouv.repository;

import fr.bastiansmn.laponiegouv.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String mail);

    List<User> findByEmailContaining(String email);

}
