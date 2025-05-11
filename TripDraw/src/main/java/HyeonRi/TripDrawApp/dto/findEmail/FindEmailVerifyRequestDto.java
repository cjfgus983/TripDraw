package HyeonRi.TripDrawApp.dto.findEmail;

import lombok.Data;

@Data
public class FindEmailVerifyRequestDto {
    private String name;
    private String email;
    private String code;
}
