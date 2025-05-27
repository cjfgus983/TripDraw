package HyeonRi.TripDrawApp.mapper.board.tripPlan;

import java.util.List;
import org.apache.ibatis.annotations.*;
import HyeonRi.TripDrawApp.dto.tripPlan.TripLocationDto;

@Mapper
public interface TripLocationMapper {

    @Insert("""
        INSERT INTO trip_location
          (plan_code, day_no, address_name, address_category, start_time, end_time, lat, lng)
        VALUES
          (#{planCode}, #{dayNo}, #{addressName}, #{addressCategory}, #{startTime}, #{endTime}, #{lat}, #{lng})
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
        "INSERT INTO trip_location(plan_code, day_no, address_name, address_category, start_time, end_time,lat,lng) VALUES",
        "<foreach collection='list' item='loc' separator=','>",
          "(",
            "#{planCode}, ",
            "#{loc.dayNo}, ",
            "#{loc.addressName}, ",
            "#{loc.addressCategory}, ",
            "#{loc.startTime}, ",
            "#{loc.endTime}, ",
            "#{loc.lat}, ",
            "#{loc.lng} ",
          ")",
        "</foreach>",
        "</script>"
      })
      void insertLocations(
        @Param("planCode") String planCode,
        @Param("list")     List<TripLocationDto> list
      );
    /**
     * planCode 로 조회해서 day_no, start_time 순으로 정렬
     */
    @Select("""
      SELECT
        location_id     AS locationId,
        plan_code       AS planCode,
        day_no          AS dayNo,
        address_name    AS addressName,
        address_category AS addressCategory,
        start_time      AS startTime,
        end_time        AS endTime
      FROM trip_location
      WHERE plan_code = #{planCode}
      ORDER BY day_no, start_time
      """)
    List<TripLocationDto> findByPlanCode(@Param("planCode") String planCode);
    
    @Select("""
    	      SELECT
    	        location_id      AS locationId,
    	        plan_code        AS planCode,
    	        day_no           AS dayNo,
    	        address_name     AS addressName,
    	        address_category AS addressCategory,
    	        DATE_FORMAT(start_time, '%H:%i:%s') AS startTime,
    	        DATE_FORMAT(end_time,   '%H:%i:%s') AS endTime,
    	       	lat AS lat, lng AS lng
    	      FROM trip_location
    	      WHERE plan_code = #{planCode}
    	      ORDER BY day_no, start_time
    	      """)
    List<TripLocationDto> selectByPlanCode(@Param("planCode") String planCode);
}
