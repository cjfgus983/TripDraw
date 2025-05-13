package HyeonRi.TripDrawApp.mapper.board;

import HyeonRi.TripDrawApp.dto.board.notice.NoticeDto;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface NoticeMapper {

    @Insert("INSERT INTO notice (user_id, title, content, created_at, view_count, is_pinned) " +
            "VALUES (#{userId}, #{title}, #{content}, #{createdAt}, #{viewCount}, #{isPinned})")
    int insertNotice(NoticeDto dto);

    @Select("SELECT * FROM notice WHERE notice_id = #{id}")
    NoticeDto selectNotice(Long id);

    @Select("SELECT * FROM notice ORDER BY is_pinned DESC, created_at DESC")
    List<NoticeDto> selectAllNotices();

    @Update("UPDATE notice SET title=#{title}, content=#{content}, is_pinned=#{isPinned} WHERE notice_id = #{noticeId}")
    int updateNotice(NoticeDto dto);

    @Delete("DELETE FROM notice WHERE notice_id = #{id}")
    int deleteNotice(Long id);
}
