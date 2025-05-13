package HyeonRi.TripDrawApp.dto.board.contact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactCommentDto {
    @NonNull
    private Long commentId;
    @NonNull
    private Long contactId;
    @NonNull
    private Long userId;
    @NonNull
    private String content;
    @NonNull
    private LocalDateTime createdAt;
}
