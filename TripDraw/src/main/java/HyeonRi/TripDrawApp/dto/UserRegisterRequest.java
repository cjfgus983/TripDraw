package HyeonRi.TripDrawApp.dto;

import HyeonRi.TripDrawApp.domain.LoginType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterRequest {
    @Email
    @NotBlank
    private String email;

    @NotBlank @Size(min = 8)
    private String password;

    @NotBlank
    private String name;

    private String phoneNumber;
    private String address;
    private String nickname;


    private LoginType loginType;   // GENERAL, KAKAO, GOOGLE
}
