package fr.bastiansmn.laponiegouv.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "wish")
@Data
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "link", nullable = false)
    private String link;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Integer price; // Price in cents

    @Column(name = "comment")
    private String comment;

    @Column(name = "gifted", nullable = false, columnDefinition = "boolean default false")
    private boolean gifted;

    @Column(name = "gifter")
    private String gifter;

    @PrePersist
    public void prePersist() {
        this.gifted = false;
    }

}
