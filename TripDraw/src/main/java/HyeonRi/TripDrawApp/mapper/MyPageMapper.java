// src/main/java/HyeonRi/TripDrawApp/mapper/MyPageMapper.java
package HyeonRi.TripDrawApp.mapper;

import HyeonRi.TripDrawApp.dto.board.drawing.DrawingDto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MyPageMapper {

    @Select("""
        SELECT
            drawing_id      AS drawingId,
          original_image_url   AS originalImageUrl,
          gpt_image_url        AS gptImageUrl,
          created_at    AS createdAt
        FROM drawing
        WHERE user_id = #{userId}
        ORDER BY created_at DESC
        """)
    List<DrawingDto> selectByUserId(@Param("userId") Long userId);


    // ▶ 유저 소유권 확인 후 그림 삭제
    @Delete("""
        DELETE FROM drawing
        WHERE user_id = #{userId}
          AND drawing_id = #{drawingId}
    """)
    void deleteByUserIdAndDrawingId(
        @Param("userId") Long userId,
        @Param("drawingId") Long drawingId
    );
}
