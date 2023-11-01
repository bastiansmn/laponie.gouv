package fr.bastiansmn.laponiegouv.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "kid")
@Data
public class Kid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="name")
    private String name;

    @OneToMany(orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Wish> wishes;
}
