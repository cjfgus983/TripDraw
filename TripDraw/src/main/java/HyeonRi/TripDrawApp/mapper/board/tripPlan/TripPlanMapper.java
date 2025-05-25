package HyeonRi.TripDrawApp.mapper.board.tripPlan;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import HyeonRi.TripDrawApp.dto.tripPlan.TripPlanWithRouteDto;

@Mapper
public interface TripPlanMapper {
    @Select("SELECT COUNT(*) FROM trip_plans WHERE plan_code = #{planCode}")
    int countByPlanCode(String planCode);

    @Insert("""
            INSERT INTO trip_plans (plan_code, region, user_id)
            VALUES (#{planCode}, #{region}, #{userId})
        """)
        void insert(
            @Param("planCode") String planCode,
            @Param("region") String region,
            @Param("userId")   Long userId
        );
    /**
     * 내가 만든 여행 계획(=trip_plans) 중에서
     * 1일차 장소를 최대 5개까지 routeConcat 으로 묶어서 가져온다.
     */
    @Select("""
      SELECT
        p.plan_code                   AS planCode,
        DATE_FORMAT(p.created_at, '%Y-%m-%d %H:%i:%s') AS createdAt,
        p.region                      AS region,
        SUBSTRING_INDEX(
          GROUP_CONCAT(l.address_name
                       ORDER BY l.start_time
                       SEPARATOR ','),
          ',',
          5
        )                            AS routeConcat
      FROM trip_plans p
      LEFT JOIN trip_location l
        ON p.plan_code = l.plan_code
       AND l.day_no  = 1
      WHERE p.user_id = #{userId}
      GROUP BY p.plan_code
      ORDER BY p.created_at DESC
    """)
    List<TripPlanWithRouteDto> selectMyPlansWithRoute(@Param("userId") Long userId);
}