package group.sirenalertla.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Fire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //Det samme som i Siren.java.
    private double latitude;
    private double longitude;

    //Tid hvornår branden er opstået.
    private LocalDateTime time;

    //true = branden slukket, false = branden er ikke slukket.
    private boolean closed;

    //Betyder at Brand kan tilhøre flere Sirens og modsat.
    @ManyToMany
    @JoinTable(
            name = "fire_sirens",
            joinColumns = @JoinColumn(name = "fire_id"),
            inverseJoinColumns = @JoinColumn(name = "siren_id")
    )
    private Set<Siren> sirens = new HashSet<>();
}
