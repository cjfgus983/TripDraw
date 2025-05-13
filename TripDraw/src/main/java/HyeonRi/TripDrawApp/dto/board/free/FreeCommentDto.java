package HyeonRi.TripDrawApp.dto.board.free;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class FreeCommentDto {
    private Long commentId;
    private Long freeId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;
    private Boolean isDeleted;
}
