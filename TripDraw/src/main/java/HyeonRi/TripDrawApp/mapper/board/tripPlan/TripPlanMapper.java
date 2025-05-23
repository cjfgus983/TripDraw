package HyeonRi.TripDrawApp.mapper.board.tripPlan;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
}