package HyeonRi.TripDrawApp.dto.board.free;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FreeDto {
    private Long freeId;
    private Long userId;
    private String title;
    private String content;
    private Integer viewCount;
    private Integer likeCount;
    private Integer dislikeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String nickName;
    private String summary;
    
    private List<String> imageUrls;
}
