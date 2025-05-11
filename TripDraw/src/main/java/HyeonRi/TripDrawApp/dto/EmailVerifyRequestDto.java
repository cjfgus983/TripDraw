package HyeonRi.TripDrawApp.dto;

import lombok.Data;

@Data
public class EmailVerifyRequestDto {
    private String email;
    private String code;
}
