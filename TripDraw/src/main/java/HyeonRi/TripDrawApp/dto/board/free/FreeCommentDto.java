package HyeonRi.TripDrawApp.dto.board.free;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FreeCommentDto {
    private Long commentId;
    private Long freeId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;
    private Boolean isDeleted;
}
