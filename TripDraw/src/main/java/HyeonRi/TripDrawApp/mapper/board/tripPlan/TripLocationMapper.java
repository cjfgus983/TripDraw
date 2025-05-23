package HyeonRi.TripDrawApp.mapper.board.tripPlan;

import java.util.List;
import org.apache.ibatis.annotations.*;
import HyeonRi.TripDrawApp.dto.tripPlan.TripLocationDto;

@Mapper
public interface TripLocationMapper {

    @Insert("""
        INSERT INTO trip_location
          (plan_code, day_no, address_name, address_category, start_time, end_time)
        VALUES
          (#{planCode}, #{dayNo}, #{addressName}, #{addressCategory}, #{startTime}, #{endTime})
    """)
    void insert(TripLocationDto dto);

    /** 편의상 배치 삽입 메서드(단일 insert 호출 반복) */
    default void insertBatch(List<TripLocationDto> list) {
        for (TripLocationDto dto : list) {
            insert(dto);
        }
    }
    
    @Insert({
        "<script>",
        "INSERT INTO trip_location(plan_code, day_no, address_name, address_category, start_time, end_time) VALUES",
        "<foreach collection='list' item='loc' separator=','>",
          "(",
            "#{planCode}, ",
            "#{loc.dayNo}, ",
            "#{loc.addressName}, ",
            "#{loc.addressCategory}, ",
            "#{loc.startTime}, ",
            "#{loc.endTime}",
          ")",
        "</foreach>",
        "</script>"
      })
      void insertLocations(
        @Param("planCode") String planCode,
        @Param("list")     List<TripLocationDto> list
      );
}
