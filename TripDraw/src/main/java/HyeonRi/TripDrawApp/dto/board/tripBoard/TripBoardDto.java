package HyeonRi.TripDrawApp.dto.board.tripBoard;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripBoardDto {
    private Long planBoardId;
    private String planCode;
    private Long userId;
    private String boardTitle;
    private String boardContent;
    private String boardCategory;
    private LocalDateTime createdAt;
}