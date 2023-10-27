package fr.bastiansmn.laponiegouv.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "family_user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "name")
    private String name;

}
