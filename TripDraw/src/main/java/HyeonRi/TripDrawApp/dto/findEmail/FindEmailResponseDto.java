package HyeonRi.TripDrawApp.dto.findEmail;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindEmailResponseDto {
    private String email;
    private boolean found;
}
