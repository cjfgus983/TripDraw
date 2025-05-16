package HyeonRi.TripDrawApp.dto.board.contact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactImageDto {
    @NonNull
    private Long imageId;
    @NonNull
    private Long contactId;
    @NonNull
    private String imageUrl;
}
