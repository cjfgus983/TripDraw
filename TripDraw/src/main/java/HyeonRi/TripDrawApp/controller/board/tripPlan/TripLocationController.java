package HyeonRi.TripDrawApp.controller.board.tripPlan;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import HyeonRi.TripDrawApp.dto.tripPlan.SaveLocationsRequest;
import HyeonRi.TripDrawApp.service.board.tripPlan.TripLocationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/trip-locations")
@RequiredArgsConstructor
public class TripLocationController {

    private final TripLocationService service;

    @PostMapping("/save")
    public ResponseEntity<Void> saveLocations(@RequestBody SaveLocationsRequest req) {
        service.saveAll(req.getPlanCode(), req.getItinerary());
        return ResponseEntity.ok().build();
    }

    
}

