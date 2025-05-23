package HyeonRi.TripDrawApp.service.board.tripPlan;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import HyeonRi.TripDrawApp.dto.tripPlan.TripLocationDto;
import HyeonRi.TripDrawApp.dto.tripPlan.TripPlanWithLocationsRequest;
import HyeonRi.TripDrawApp.mapper.board.tripPlan.TripLocationMapper;
import HyeonRi.TripDrawApp.mapper.board.tripPlan.TripPlanMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripPlanService {
	private final TripPlanMapper     planMapper;
    private final TripLocationMapper locMapper;

    private final SecureRandom rnd = new SecureRandom();
    private static final String ALPHA_NUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /** 
     * 랜덤 알파벳+숫자 조합(planCode) 생성 후 중복 검사 → 저장 
     */
    @Transactional
    public String createPlanWithLocations(TripPlanWithLocationsRequest req) {
        // 1) 랜덤 planCode 생성 (중복 체크 로직 추가해도 좋습니다)
        String planCode;
        do {
        	planCode = generateCode(10);
        } while (planMapper.countByPlanCode(planCode) > 0);

     // 2) trip_plans 에 저장
        planMapper.insert(
            planCode,
            req.getRegion(),
            req.getUserId()
        );

        final String finalPlanCode = planCode;
        
        // 3) locations 에 planCode 세팅 후 일괄 저장
        List<TripLocationDto> withCode = req.getLocations().stream()
            .peek(loc -> loc.setPlanCode(finalPlanCode))
            .collect(Collectors.toList());
        locMapper.insertLocations(planCode, withCode);

        return planCode;
    }

    private String generateCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHA_NUM.charAt(rnd.nextInt(ALPHA_NUM.length())));
        }
        return sb.toString();
    }
}