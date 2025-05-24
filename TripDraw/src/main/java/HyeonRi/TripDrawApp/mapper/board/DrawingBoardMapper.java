// src/main/java/HyeonRi/TripDrawApp/mapper/board/DrawingBoardMapper.java
package HyeonRi.TripDrawApp.mapper.board;

import HyeonRi.TripDrawApp.domain.DrawingBoard;
import HyeonRi.TripDrawApp.dto.board.drawing.DrawingBoardDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DrawingBoardMapper {

    /** 전체 게시물 조회 (닉네임 포함) */
    @Select("""
        SELECT
            db.drawing_board_id      AS drawingBoardId,
            db.drawing_id            AS drawingId,
            db.user_id               AS userId,
            u.nickname               AS nickname,
            db.title,
            db.content,
            db.view_count            AS viewCount,
            db.like_count            AS likeCount,
            db.created_at            AS createdAt,
            db.updated_at            AS updatedAt,
            d.original_image_url     AS imageUrl
        FROM drawing_board db
        JOIN drawing d ON db.drawing_id = d.drawing_id
        JOIN `user` u ON db.user_id = u.user_id
        ORDER BY db.created_at DESC
    """)
    List<DrawingBoardDto> selectAll();


    /** 게시물 삭제 전에 연관된 좋아요 기록 모두 삭제 */
    @Delete("""
        DELETE FROM user_drawing_like
         WHERE drawing_board_id = #{boardId}
    """)
    void deleteLikesByBoardId(@Param("boardId") Long boardId);


    /** 게시물 삭제 */
    @Delete("""
        DELETE FROM drawing_board
         WHERE drawing_board_id = #{boardId}
    """)
    int deleteById(@Param("boardId") Long boardId);


    /** 지난 7일간 좋아요 수 상위 9개 (인기 작품) */
    @Select("""
        SELECT
            db.drawing_board_id      AS drawingBoardId,
            db.drawing_id            AS drawingId,
            db.user_id               AS userId,
            u.nickname               AS nickname,
            db.title,
            db.content,
            db.view_count            AS viewCount,
            db.like_count            AS likeCount,
            db.created_at            AS createdAt,
            db.updated_at            AS updatedAt,
            d.original_image_url     AS imageUrl
        FROM drawing_board db
        JOIN drawing d ON db.drawing_id = d.drawing_id
        JOIN `user` u ON db.user_id = u.user_id
        WHERE db.created_at >= DATE_SUB(NOW(), INTERVAL 7 DAY)
        ORDER BY db.like_count DESC
        LIMIT 9
    """)
    List<DrawingBoardDto> selectPopular();

    /** 페이지네이션 조회 */
    @Select("""
        SELECT
            db.drawing_board_id      AS drawingBoardId,
            db.drawing_id            AS drawingId,
            db.user_id               AS userId,
            u.nickname               AS nickname,
            db.title,
            db.content,
            db.view_count            AS viewCount,
            db.like_count            AS likeCount,
            db.created_at            AS createdAt,
            db.updated_at            AS updatedAt,
            d.original_image_url     AS imageUrl
        FROM drawing_board db
        JOIN drawing d ON db.drawing_id = d.drawing_id
        JOIN `user` u ON db.user_id = u.user_id
        ORDER BY db.created_at DESC
        LIMIT #{size} OFFSET #{offset}
    """)
    List<DrawingBoardDto> selectPage(
            @Param("offset") int offset,
            @Param("size")   int size
    );

    @Select("SELECT * FROM drawing_board WHERE drawing_board_id = #{id}")
    DrawingBoardDto selectById(Long id);

    @Insert("""
        INSERT INTO drawing_board
            (drawing_id, user_id, title, content)
        VALUES
            (#{drawingId}, #{userId}, #{title}, #{content})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "drawingBoardId")
    int insert(DrawingBoardDto dto);

    @Update("""
        UPDATE drawing_board
           SET title      = #{title},
               content    = #{content},
               updated_at = NOW()
         WHERE drawing_board_id = #{drawingBoardId}
    """)
    int update(DrawingBoardDto dto);

    @Delete("DELETE FROM drawing_board WHERE drawing_board_id = #{id}")
    int delete(Long id);

    @Update("UPDATE drawing_board SET view_count = view_count + 1 WHERE drawing_board_id = #{id}")
    int incrementViewCount(Long id);

    @Update("UPDATE drawing_board SET like_count = like_count + 1 WHERE drawing_board_id = #{id}")
    int incrementLikeCount(Long id);

    /** 중복 좋아요 방지용 조회 */
    @Select("""
        SELECT COUNT(*) > 0
          FROM user_drawing_like
         WHERE user_id = #{userId}
           AND drawing_board_id = #{boardId}
    """)
    boolean existsLike(
            @Param("userId")  Long userId,
            @Param("boardId") Long boardId
    );

    /** 좋아요 기록 삽입 */
    @Insert("""
        INSERT INTO user_drawing_like
            (user_id, drawing_board_id)
        VALUES
            (#{userId}, #{boardId})
    """)
    void insertLike(
            @Param("userId")  Long userId,
            @Param("boardId") Long boardId
    );

    @Insert("""
        INSERT INTO drawing_board
            (drawing_id, user_id, title, content)
        VALUES
            (#{drawingId}, #{userId}, #{title}, #{content})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "drawingBoardId")
    void insertDrawingBoard(DrawingBoard board);
}
