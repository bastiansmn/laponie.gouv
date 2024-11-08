package fr.bastiansmn.laponiegouv.model;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "family")
@Data
public class Family {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "link_family_user",
            joinColumns = @JoinColumn(name = "family_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "link_family_kid",
            joinColumns = @JoinColumn(name = "family_id"),
            inverseJoinColumns = @JoinColumn(name = "kid_id")
    )
    private List<Kid> kids;

    @Column(name = "draw", columnDefinition = "jsonb")
    @Type(JsonBinaryType.class)
    private Map<String, String> draw;

}
