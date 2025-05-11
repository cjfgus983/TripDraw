// src/main/java/HyeonRi/TripDrawApp/dto/UserProfileDto.java
package HyeonRi.TripDrawApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileDto {
    private Long   userId;
    private String email;
    private String name;
    private String phoneNumber;
    private String address;
    private String nickname;
    private String role;
}
