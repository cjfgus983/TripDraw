package HyeonRi.TripDrawApp.dto.tripPlan;

import lombok.Data;

@Data
public class TripPlanWithRouteDto {
    private String planCode;     // plancode
    private String createdAt;    // "YYYY-MM-DD HH:mm:ss"
    private String region;       // 여행 지역
    private String routeConcat;  // "장소1,장소2,..." (최대 5개)
}