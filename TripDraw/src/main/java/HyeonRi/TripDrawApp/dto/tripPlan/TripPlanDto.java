package HyeonRi.TripDrawApp.dto.tripPlan;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripPlanDto {
    private String planCode;
    private Long userId;
    private String region;
    private LocalDateTime createdAt;
}