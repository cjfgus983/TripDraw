package HyeonRi.TripDrawApp.controller.board.tripPlan;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import HyeonRi.TripDrawApp.dto.tripPlan.DailyPlanResponse;
import HyeonRi.TripDrawApp.dto.tripPlan.ItineraryItemDto;
import HyeonRi.TripDrawApp.dto.tripPlan.ItineraryUpdateRequestDto;
import HyeonRi.TripDrawApp.dto.tripPlan.TripPlanWithLocationsRequest;
import HyeonRi.TripDrawApp.service.AiService;
import HyeonRi.TripDrawApp.service.AiService.NearbyPlacesResult;
import HyeonRi.TripDrawApp.service.board.tripPlan.TripLocationService;
import HyeonRi.TripDrawApp.service.board.tripPlan.TripPlanService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TripPlanController {
    private final AiService aiService;
    private final TripLocationService locService;
    private final TripPlanService service;

    /**
     * POST /api/itinerary
     * body 예시:
     * {
     *   "places": ["후지산, 일본"],
     *   "days": 3
     * }
     */
    @PostMapping("/itinerary")
    public ResponseEntity<List<List<ItineraryItemDto>>> createItinerary(
            @RequestBody ItineraryRequest request
    ) throws IOException {
    	List<List<ItineraryItemDto>> result =
            aiService.planItinerary(request.getPlaces(), request.getDays());
        return ResponseEntity.ok(result);
    }
//    public ResponseEntity<List<List<ItineraryItemDto>>> createItinerary(
//            @RequestBody ItineraryRequest request
//    ) throws IOException {
//        List<List<ItineraryItemDto>> plan = aiService.planItinerary(request.getPlaces(), request.getDays());
//        return ResponseEntity.ok(plan);
//    }
    @GetMapping("/nearby")
    public NearbyPlacesResult getNearby(
        @RequestParam double lat,
        @RequestParam double lng
    ) throws IOException {
        // 최소 10개 보장
        return aiService.fetchFlexibleNearbyPlaces(lat, lng, 10);
    }

    @PostMapping("/itinerary/update-times")
    public List<List<ItineraryItemDto>> updateTimes(
        @RequestBody ItineraryUpdateRequestDto req
    ) throws IOException {
        return aiService.recalculateItineraryTimes(req.getItinerary());
    }
    
    @PostMapping("/plans-with-locations")
    public ResponseEntity<String> createPlanAndLocations(
            @RequestBody TripPlanWithLocationsRequest req
    ) {
    	System.out.println(req.getLocations().get(0).getLat());
        String planCode = service.createPlanWithLocations(req);
        return ResponseEntity.ok(planCode);
    }
    
    /**
     * Plan Code 하나로 해당 계획의 일차별 장소들을 묶어서 반환
     */
    @GetMapping("/plans-with-locations/{planCode}")
    public ResponseEntity<DailyPlanResponse> getByPlanCode(@PathVariable String planCode) {
        var dailyPlans = locService.getDailyPlans(planCode);
        return ResponseEntity.ok(new DailyPlanResponse(dailyPlans));
    }

    @Data
    public static class ItineraryRequest {
        private List<String> places;
        private int days;
    }
}
