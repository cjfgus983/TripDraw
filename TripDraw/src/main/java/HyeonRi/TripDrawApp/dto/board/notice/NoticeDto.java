package HyeonRi.TripDrawApp.dto.board.notice;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDto {
    private Long noticeId;

    @NonNull
    private Long userId;
    
    private String nickname;

    @NonNull
    private String title;

    private String content;
    private String contentHtml;

    @NonNull
    private LocalDateTime createdAt;

    private Integer viewCount;
    private Boolean isPinned;
}
