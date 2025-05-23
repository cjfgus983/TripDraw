package HyeonRi.TripDrawApp.dto.tripPlan;

import java.util.List;

import HyeonRi.TripDrawApp.controller.board.tripPlan.TripLocationController;
import lombok.Data;

@Data
public class SaveLocationsRequest {
    private String planCode;
    private List<List<ItineraryItemDto>> itinerary;
}
