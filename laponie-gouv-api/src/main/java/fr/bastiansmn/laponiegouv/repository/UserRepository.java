package fr.bastiansmn.laponiegouv.repository;

import fr.bastiansmn.laponiegouv.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
