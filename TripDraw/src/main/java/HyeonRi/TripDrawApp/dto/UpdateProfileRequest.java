package HyeonRi.TripDrawApp.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String email; // 변경 불가
    private String phoneNumber; // 변경 불가
    private String address; // 변경 가능
    private String addressDetail; // 변경 가능
    private String nickname; // 변경 가능
    private String newPassword; // 변경 가능
}
