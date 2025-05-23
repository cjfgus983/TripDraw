// src/main/java/HyeonRi/TripDrawApp/dto/UserProfileDto.java
package HyeonRi.TripDrawApp.dto;

import HyeonRi.TripDrawApp.domain.LoginType;
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
    private String addressDetail;
    private String nickname;
    private String role;
    private LoginType loginType;
}
