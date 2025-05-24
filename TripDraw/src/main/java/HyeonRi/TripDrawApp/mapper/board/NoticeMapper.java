package HyeonRi.TripDrawApp.mapper.board;

import HyeonRi.TripDrawApp.dto.board.notice.NoticeDto;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface NoticeMapper {

    @Insert("INSERT INTO notice (user_id, title, content, created_at, view_count, is_pinned) " +
            "VALUES (#{userId}, #{title}, #{content}, NOW(),  0, #{isPinned})")
    int insertNotice(NoticeDto dto);

    @Select("""
    	    SELECT
    	      n.notice_id    AS noticeId,
    	      n.user_id      AS userId,
    	      u.nickname     AS nickname,
    	      n.title,
    	      n.content,
    	      n.created_at   AS createdAt,
    	      n.view_count   AS viewCount,
    	      n.is_pinned    AS isPinned
    	    FROM notice n
    	    JOIN `user` u
    	      ON n.user_id = u.user_id
    	    WHERE n.notice_id = #{id}
    	    """)
    NoticeDto selectNotice(Long id);

    @Select("""
    	      SELECT
    	        n.notice_id   AS noticeId,
    	        n.user_id     AS userId,
    	        u.nickname    AS nickname,
    	        n.title       AS title,
    	        n.content     AS content,
    	        n.created_at  AS createdAt,
    	        n.view_count  AS viewCount,
    	        n.is_pinned   AS isPinned
    	      FROM notice n
    	      JOIN `user` u
    	        ON n.user_id = u.user_id
    	      ORDER BY n.is_pinned DESC, n.created_at DESC
    	      """)
    List<NoticeDto> selectAllNotices();

    @Update("UPDATE notice SET title=#{title}, content=#{content}, is_pinned=#{isPinned} WHERE notice_id = #{noticeId}")
    int updateNotice(NoticeDto dto);

    @Delete("DELETE FROM notice WHERE notice_id = #{id}")
    int deleteNotice(Long id);
    
    @Update("UPDATE notice SET view_count = view_count + 1 WHERE notice_id = #{noticeId}")
    void incrementViewCount(@Param("noticeId") Long noticeId);
}
