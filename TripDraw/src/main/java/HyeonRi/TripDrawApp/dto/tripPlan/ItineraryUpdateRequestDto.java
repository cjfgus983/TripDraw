package HyeonRi.TripDrawApp.dto.tripPlan;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ItineraryUpdateRequestDto {
    private List<List<ItineraryItemDto>> itinerary;
}
