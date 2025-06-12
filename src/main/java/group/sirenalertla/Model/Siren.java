package group.sirenalertla.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Siren {

    //Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //Nord(positiv)/Syd(negativ) længdegrad nord/syd for ækvator.
    private double latitude;
    //Øst(Positiv)/Vest(Negativ) længdegrad vest/øst for London.
    private double longitude;

    // True = sirene er ud af drift, false = sirene er i fuld drift.
    private boolean disabled;

    // FARE og FRED enum
    @Enumerated(EnumType.STRING)
    private Status status;
}
