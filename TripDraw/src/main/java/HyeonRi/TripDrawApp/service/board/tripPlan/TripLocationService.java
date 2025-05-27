package HyeonRi.TripDrawApp.service.board.tripPlan;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import HyeonRi.TripDrawApp.dto.tripPlan.DayPlanDto;
import HyeonRi.TripDrawApp.dto.tripPlan.ItineraryItemDto;
import HyeonRi.TripDrawApp.dto.tripPlan.TripLocationDto;
import HyeonRi.TripDrawApp.mapper.board.tripPlan.TripLocationMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripLocationService {

    private final TripLocationMapper mapper;
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

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
    
    /**
     * planCode 로 저장된 모든 TripLocation 을 꺼내서
     * dayNo, startTime 순으로 정렬하여 반환
     */
    public List<TripLocationDto> getByPlanCode(String planCode) {
        return mapper.findByPlanCode(planCode);
    }
    
    @Transactional(readOnly = true)
    public List<DayPlanDto> getDailyPlans(String planCode) {
        // DB에서 planCode 로 모든 위치를 가져와서
        List<TripLocationDto> all = mapper.selectByPlanCode(planCode);

        // dayNo 별로 그룹핑
        Map<Integer, List<TripLocationDto>> byDay =
            all.stream().collect(Collectors.groupingBy(TripLocationDto::getDayNo,
                                                       LinkedHashMap::new,
                                                       Collectors.toList()));

        // DTO 변환
        return byDay.entrySet().stream()
            .map(e -> {
                List<ItineraryItemDto> items = e.getValue().stream()
                    .map(loc -> {
                        ItineraryItemDto it = new ItineraryItemDto();
                        it.setName(loc.getAddressName());
                        it.setCategory(loc.getAddressCategory());
                        // LocalTime → "HH:mm" 문자열로 변환
                        it.setStartTime(loc.getStartTime().format(TIME_FMT));
                        it.setEndTime(  loc.getEndTime()  .format(TIME_FMT));
                        it.setLat(loc.getLat());
                        it.setLng(loc.getLng());
                        return it;
                    })
                    .collect(Collectors.toList());

                return new DayPlanDto(e.getKey(), items);
            })
            .collect(Collectors.toList());
    }
}

