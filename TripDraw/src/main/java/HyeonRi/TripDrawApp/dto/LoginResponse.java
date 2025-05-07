package HyeonRi.TripDrawApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;      // JWT
    private String tokenType;  // e.g. "Bearer"
}