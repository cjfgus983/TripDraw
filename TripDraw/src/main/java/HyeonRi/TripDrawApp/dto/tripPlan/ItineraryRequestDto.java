package HyeonRi.TripDrawApp.dto.tripPlan;

import java.util.List;

//요청 DTO
public record ItineraryRequestDto(
 List<String> places,
 int days
) {}
