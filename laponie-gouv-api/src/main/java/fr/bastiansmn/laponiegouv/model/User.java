package fr.bastiansmn.laponiegouv.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.bastiansmn.laponiegouv.model.enums.Gender;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

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

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Family> family;

    @OneToMany(orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Wish> wishes;

    @Column(name = "sexe")
    @Enumerated(EnumType.STRING)
    private Gender sexe;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof User))
            return false;
        if (obj == this)
            return true;
        return this.getId().equals(((User) obj).getId());
    }

    @Override
    public int hashCode() {
        return this.getEmail().hashCode();
    }
}
