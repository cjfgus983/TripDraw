package HyeonRi.TripDrawApp.dto.board.tripBoard;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripCommentDto {
    private Long  commentId;
    private Long  planBoardId;
    private Long  userId;
    private String content;
    private LocalDateTime createdAt;
    private Boolean isDeleted;
}