package fr.bastiansmn.laponiegouv.repository;

import fr.bastiansmn.laponiegouv.model.Kid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KidRepository extends JpaRepository<Kid, Long> {

}
