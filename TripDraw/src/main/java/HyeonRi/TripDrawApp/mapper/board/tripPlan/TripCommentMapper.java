package HyeonRi.TripDrawApp.mapper.board.tripPlan;

import java.util.List;
import org.apache.ibatis.annotations.*;

import HyeonRi.TripDrawApp.dto.board.tripBoard.TripCommentDto;

@Mapper
public interface TripCommentMapper {

    @Insert("""
        INSERT INTO trip_comment (plan_board_id, user_id, content, created_at, is_deleted)
        VALUES (#{planBoardId}, #{userId}, #{content}, #{createdAt}, #{isDeleted})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "commentId")
    void insertComment(TripCommentDto dto);

    @Select("""
        SELECT * 
          FROM trip_comment 
         WHERE plan_board_id = #{planBoardId}
           AND is_deleted = FALSE
         ORDER BY created_at ASC
    """)
    List<TripCommentDto> selectCommentsByBoardId(Long planBoardId);

    @Update("""
        UPDATE trip_comment
           SET content = #{content}
         WHERE comment_id    = #{commentId}
           AND plan_board_id = #{planBoardId}
    """)
    void updateComment(TripCommentDto dto);

    @Update("""
        UPDATE trip_comment
           SET is_deleted = TRUE
         WHERE comment_id    = #{commentId}
           AND plan_board_id = #{planBoardId}
    """)
    void deleteComment(@Param("planBoardId") Long planBoardId,
                       @Param("commentId")    Long commentId);
}
