package HyeonRi.TripDrawApp.dto.board.contact;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
public class ContactDto {
    private Integer contactId;

    @NonNull
    private Long userId;

    @NonNull
    private String title;

    @NonNull
    private String content;

    @NonNull
    private Boolean isSecret;

    @NonNull
    private Integer viewCount;

    @NonNull
    private LocalDateTime createdAt;
}
