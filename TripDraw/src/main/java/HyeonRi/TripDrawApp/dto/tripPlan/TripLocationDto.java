package HyeonRi.TripDrawApp.dto.tripPlan;

import java.time.LocalTime;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripLocationDto {
    // locationId 는 AUTO_INCREMENT 이므로 insert 시에는 생략해도 됩니다.
    //private Long    locationId;
    private String  planCode;
    private Integer dayNo;
    private String  addressName;
    private String  addressCategory;
    private LocalTime startTime;
    private LocalTime endTime;
}
