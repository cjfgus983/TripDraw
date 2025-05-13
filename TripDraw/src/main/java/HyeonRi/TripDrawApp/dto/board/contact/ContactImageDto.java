package HyeonRi.TripDrawApp.dto.board.contact;

import lombok.Data;
import lombok.NonNull;

@Data
public class ContactImageDto {
    private Integer imageId;

    @NonNull
    private Integer contactId;

    @NonNull
    private String imageUrl;
}
