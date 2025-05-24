package HyeonRi.TripDrawApp.domain;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class DrawingBoard {
    private Long drawingBoardId;
    private Long drawingId;
    private Long userId;
    private String title;
    private String content;            // null 허용
    private Integer viewCount;
    private Integer likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
