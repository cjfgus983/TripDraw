package HyeonRi.TripDrawApp.dto.tripPlan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryItemDto {
    private String name;       // 장소 이름
    private String category;   // "tour" | "breakfast" | "lunch" | "cafe" | "dinner"
    private String startTime;  // "HH:mm"
    private String endTime;    // "HH:mm"

}