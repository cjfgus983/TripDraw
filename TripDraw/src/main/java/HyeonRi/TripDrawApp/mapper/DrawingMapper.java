package HyeonRi.TripDrawApp.mapper;

import org.apache.ibatis.annotations.*;
import HyeonRi.TripDrawApp.dto.board.drawing.DrawingDto;

@Mapper
public interface DrawingMapper {
    @Insert("""
    INSERT INTO drawing
      (user_id, recommend_location, original_image_url, gpt_image_url)
    VALUES
      (#{userId}, #{recommendLocation}, #{originalImageUrl}, #{gptImageUrl})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "drawingId")
    int insertDrawing(DrawingDto dto);
}