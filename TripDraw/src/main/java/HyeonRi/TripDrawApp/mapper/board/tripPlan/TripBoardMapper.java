package HyeonRi.TripDrawApp.mapper.board.tripPlan;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import HyeonRi.TripDrawApp.dto.board.tripBoard.TripBoardDto;
import HyeonRi.TripDrawApp.dto.board.tripBoard.TripBoardWithRouteDto;

@Mapper
public interface TripBoardMapper {
	@Select("""
			  SELECT
			    b.plan_board_id           AS planBoardId,
			    b.board_title             AS boardTitle,
			    b.board_content           AS boardContent,
			    -- 콤마로 결합된 카테고리 문자열
			    b.board_category          AS boardCategoryConcat,
			    b.plan_code               AS planCode,
			    DATE_FORMAT(b.created_at, '%Y-%m-%d %H:%i:%s') AS createdAt,
			    -- 작성자 닉네임
			    u.nickname                AS nickname,
			    -- 1일차 장소 최대 5개
			    SUBSTRING_INDEX(
			      GROUP_CONCAT(l.address_name ORDER BY l.start_time SEPARATOR ','),
			      ',',
			      5
			    )                         AS routeConcat,
			    p.region				  AS region
			  FROM trip_board b
			  -- 작성자 정보 조인
			  LEFT JOIN trip_plans p
			  	ON b.plan_code = p.plan_code
			  LEFT JOIN `user` u
			    ON b.user_id = u.user_id
			  -- 1일차 장소 정보 조인
			  LEFT JOIN trip_location l
			    ON b.plan_code = l.plan_code
			   AND l.day_no  = 1
			  GROUP BY b.plan_board_id
			  ORDER BY b.created_at DESC
			""")
	List<TripBoardWithRouteDto> selectBoardsWithRoute();


    @Insert("""
        INSERT INTO trip_board (plan_code, user_id, board_title, board_content, board_category)
        VALUES (#{planCode}, #{userId}, #{boardTitle}, #{boardContent}, #{boardCategory})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "planBoardId")
    void insertBoard(TripBoardDto dto);

    @Select("SELECT * FROM trip_board WHERE plan_board_id = #{planBoardId}")
    TripBoardWithRouteDto selectBoardById(Long planBoardId);

    @Select("SELECT * FROM trip_board ORDER BY created_at DESC")
    List<TripBoardDto> selectAllBoards();

    @Update("""
        UPDATE trip_board
           SET board_title    = #{boardTitle},
               board_content  = #{boardContent},
               board_category = #{boardCategory}
         WHERE plan_board_id = #{planBoardId}
    """)
    void updateBoard(TripBoardDto dto);

    @Delete("DELETE FROM trip_board WHERE plan_board_id = #{planBoardId}")
    void deleteBoard(Long planBoardId);
}