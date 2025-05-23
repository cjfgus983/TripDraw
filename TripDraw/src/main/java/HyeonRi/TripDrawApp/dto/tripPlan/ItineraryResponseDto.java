package HyeonRi.TripDrawApp.dto.tripPlan;

import java.util.List;

//응답 DTO
public record ItineraryResponseDto(
 List<List<String>> itinerary
) {}