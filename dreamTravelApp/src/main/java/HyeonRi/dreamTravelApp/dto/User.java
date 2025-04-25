package HyeonRi.dreamTravelApp.dto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    private boolean emailVerified;

    @Column(nullable = false, unique = true)
    private String nickname;
}
