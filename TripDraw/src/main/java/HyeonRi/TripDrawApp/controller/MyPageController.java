// src/main/java/HyeonRi/TripDrawApp/controller/MyPageController.java
package HyeonRi.TripDrawApp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import HyeonRi.TripDrawApp.dto.DrawingResult;
import HyeonRi.TripDrawApp.dto.tripPlan.TripPlanWithRouteDto;
import HyeonRi.TripDrawApp.security.CustomUserDetails;
import HyeonRi.TripDrawApp.service.MyPageService;
import HyeonRi.TripDrawApp.service.board.tripPlan.TripPlanService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    private final MyPageService myPageService;
    private final TripPlanService tripPlanService;
    

    // 로그인된 유저의 그림 목록 조회
    @GetMapping("/drawings")
    public List<DrawingResult> getMyDrawings(
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        Long userId = principal.getUser().getUserId();
        return myPageService.getDrawingsByUser(userId);
    }

    // ▶ 그림 삭제
    @DeleteMapping("/drawings/{drawingId}")
    public void deleteDrawing(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable Long drawingId
    ) {
        Long userId = principal.getUser().getUserId();
        myPageService.deleteDrawing(userId, drawingId);
    }
    
    @GetMapping("/trip-plans")
    public ResponseEntity<List<TripPlanWithRouteDto>> getMyPlans(
            @RequestParam("userId") Long userId
    ) {
    	System.out.println(userId);
        List<TripPlanWithRouteDto> list = tripPlanService.getMyPlans(userId);
        return ResponseEntity.ok(list);
    }
    /** 내 계획 삭제 **/
    @DeleteMapping("/trip-plans/{planCode}")
    public ResponseEntity<Void> deleteMyPlan(
        @PathVariable String planCode,
        @RequestParam Long userId
    ) {
    	myPageService.deleteMyPlan(userId, planCode);
      return ResponseEntity.ok().build();
    }
}
