package HyeonRi.TripDrawApp.dto.board.tripBoard;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import HyeonRi.TripDrawApp.dto.board.tripBoard.BoardWithLocationsDto.DayPlan;
import HyeonRi.TripDrawApp.dto.tripPlan.TripLocationDto;
import lombok.Data;

@Data
public class BoardWithLocationsDto {
  private TripBoardWithRouteDto board;
  private List<DayPlan> dailyPlans;

  public BoardWithLocationsDto(TripBoardWithRouteDto b, Map<Integer,List<TripLocationDto>> days) {
    this.board = b;
    this.dailyPlans = days.entrySet().stream()
      .map(e -> new DayPlan(e.getKey(), e.getValue()))
      .collect(Collectors.toList());
  }

  @Data
  public static class DayPlan {
    private int dayNo;
    private List<TripLocationDto> activities;
    public DayPlan(int dayNo, List<TripLocationDto> acts) {
      this.dayNo = dayNo;
      this.activities = acts;
    }
  }
}