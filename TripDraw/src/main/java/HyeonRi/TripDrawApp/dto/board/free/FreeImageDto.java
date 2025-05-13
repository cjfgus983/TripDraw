package HyeonRi.TripDrawApp.dto.board.free;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class FreeImageDto {
    private Long imageId;
    private Long freeId;
    private String imageUrl;
}