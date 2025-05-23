package HyeonRi.TripDrawApp.dto.tripPlan;

import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyPlanResponse {
    private List<DayPlanDto> dailyPlans;
}
