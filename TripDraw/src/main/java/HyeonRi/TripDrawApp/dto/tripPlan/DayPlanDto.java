package HyeonRi.TripDrawApp.dto.tripPlan;

import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayPlanDto {
    private int dayNo;
    private List<ItineraryItemDto> activities;
}
