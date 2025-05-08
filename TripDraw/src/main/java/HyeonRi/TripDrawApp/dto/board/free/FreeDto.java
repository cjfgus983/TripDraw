package HyeonRi.TripDrawApp.dto.board.free;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FreeDto {
    private Long freeId;
    private Long userId;
    private String title;
    private String content;
    private Integer viewCount;
    private Integer likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
