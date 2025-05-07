package HyeonRi.TripDrawApp.dto;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String address;
    private String nickname;
    private boolean isKakaoLogin;
    private String role;
}
