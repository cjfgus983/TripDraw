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
			    -- 1일차 장소 이름의 콤마 앞 부분만 5개 뽑아서 콤마로 이어 붙임
			  (
			    SELECT
			      GROUP_CONCAT(
			        SUBSTRING_INDEX(address_name, ',', 1)
			        ORDER BY start_time
			        SEPARATOR ','
			      )
			    FROM (
			      SELECT address_name, start_time
			      FROM trip_location
			      WHERE plan_code = b.plan_code
			        AND day_no    = 1
			      ORDER BY start_time
			      LIMIT 5
			    ) AS x
			  ) AS routeConcat,
			 p.region AS region
			
			FROM trip_board b
			LEFT JOIN trip_plans p
			  ON b.plan_code = p.plan_code
			LEFT JOIN `user` u
			  ON b.user_id   = u.user_id
			ORDER BY b.created_at DESC
			""")
	List<TripBoardWithRouteDto> selectBoardsWithRoute();


    @Insert("""
        INSERT INTO trip_board (plan_code, user_id, board_title, board_content, board_category)
        VALUES (#{planCode}, #{userId}, #{boardTitle}, #{boardContent}, #{boardCategory})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "planBoardId")
    void insertBoard(TripBoardDto dto);

    @Select("""
			 SELECT
			    b.plan_board_id           AS planBoardId,
			    b.board_title             AS boardTitle,
			    b.board_content           AS boardContent,
			    b.board_category          AS boardCategoryConcat,
			    b.plan_code               AS planCode,
			    DATE_FORMAT(b.created_at, '%Y-%m-%d %H:%i:%s') AS createdAt,
			    u.nickname                AS nickname,
			
			    -- 1일차 장소 앞부분(콤마 전) 5개만 뽑아서 콤마로 이어 붙임
			    (
			      SELECT
			        GROUP_CONCAT(
			          SUBSTRING_INDEX(tmp.address_name, ',', 1)
			          ORDER BY tmp.start_time
			          SEPARATOR ','
			        )
			      FROM (
			        SELECT address_name, start_time
			        FROM trip_location
			        WHERE plan_code = b.plan_code
			          AND day_no    = 1
			        ORDER BY start_time
			        LIMIT 5
			      ) AS tmp
			    ) AS routeConcat,
			
			    p.region AS region
			
			  FROM trip_board b
			  LEFT JOIN trip_plans p
			    ON b.plan_code = p.plan_code
			  LEFT JOIN `user` u
			    ON b.user_id   = u.user_id
			
			  WHERE b.plan_board_id = #{planBoardId}
		""")
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