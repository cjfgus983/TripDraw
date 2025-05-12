package HyeonRi.TripDrawApp.dto.board.contact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDto {
    @NonNull
    private Long contactId;
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
