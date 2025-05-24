package HyeonRi.TripDrawApp.dto.board.drawing;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DrawingDto {
    private Long drawingId;
    private Long userId;
    private String recommendLocation;
    private String originalImageUrl;
    private String gptImageUrl;
    /** 날짜만 저장할 때는 LocalDate 타입을 사용 */
    private LocalDateTime createdAt;
}
