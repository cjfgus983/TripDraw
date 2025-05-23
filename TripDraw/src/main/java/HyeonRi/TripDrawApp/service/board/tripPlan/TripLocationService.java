package HyeonRi.TripDrawApp.service.board.tripPlan;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import HyeonRi.TripDrawApp.dto.tripPlan.ItineraryItemDto;
import HyeonRi.TripDrawApp.dto.tripPlan.TripLocationDto;
import HyeonRi.TripDrawApp.mapper.board.tripPlan.TripLocationMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripLocationService {

    private final TripLocationMapper mapper;

    /**
     * planCode 와
     * ItineraryItemDto 의 2차원 리스트(일차별 리스트)를 받아
     * DB에 저장한다.
     */
    @Transactional
    public void saveAll(String planCode, List<List<ItineraryItemDto>> itinerary) {
        List<TripLocationDto> batch = new ArrayList<>();

        for (int i = 0; i < itinerary.size(); i++) {
            int dayNo = i + 1;
            for (ItineraryItemDto item : itinerary.get(i)) {
                TripLocationDto dto = TripLocationDto.builder()
                    .planCode(planCode)
                    .dayNo(dayNo)
                    .addressName(item.getName())
                    .addressCategory(item.getCategory())
                    // ItineraryItemDto 가 startTime/endTime 을 "HH:mm" 형태로 가지고 있다고 가정
                    .startTime(LocalTime.parse(item.getStartTime()))
                    .endTime(LocalTime.parse(item.getEndTime()))
                    .build();
                batch.add(dto);
            }
        }

        // 일괄 삽입
        mapper.insertBatch(batch);
    }
}

