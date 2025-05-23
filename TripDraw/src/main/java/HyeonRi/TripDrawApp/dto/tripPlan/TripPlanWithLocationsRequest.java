package HyeonRi.TripDrawApp.dto.tripPlan;

import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripPlanWithLocationsRequest {
    private Long userId;
    private String tripCategory;
    private String planTitle;
    private String planDescription;
    private String region;
    private List<TripLocationDto> locations;  // planCode는 null로 들어오고, 서비스에서 채워줍니다.
}