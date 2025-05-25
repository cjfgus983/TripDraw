// src/main/java/HyeonRi/TripDrawApp/dto/DrawingBoardDto.java
package HyeonRi.TripDrawApp.dto.board.drawing;

import lombok.Data;
import java.time.LocalDateTime;

// 게시판 화면 뿌려주기 위한 디티오

@Data
public class DrawingBoardDto {
    private Long        drawingBoardId;
    private Long        drawingId;
    private Long        userId;
    private String      nickname;
    private String      title;
    private String      content;
    private Integer     viewCount;
    private Integer     likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String imageUrl;
    private Boolean isLiked;
}
