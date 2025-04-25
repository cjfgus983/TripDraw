package HyeonRi.dreamTravelApp.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String email;
    private String password;
    private boolean emailVerified;

    @Enumerated(EnumType.STRING)
    private Gender gender = Gender.M; // 기본값

    private LocalDate birthDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public enum Gender {
        M, F
    }
}
