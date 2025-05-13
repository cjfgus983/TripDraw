package HyeonRi.TripDrawApp.dto.board.contact;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
public class ContactCommentDto {
    private Integer commentId;

    @NonNull
    private Integer contactId;

    @NonNull
    private Long userId; // 관리자만 작성

    @NonNull
    private String content;

    @NonNull
    private LocalDateTime createdAt;
}
