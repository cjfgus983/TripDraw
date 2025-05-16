package HyeonRi.TripDrawApp.mapper.board;

import java.util.List;

import org.apache.ibatis.annotations.*;

import HyeonRi.TripDrawApp.dto.board.free.FreeCommentDto;
import HyeonRi.TripDrawApp.dto.board.free.FreeDto;
import HyeonRi.TripDrawApp.dto.board.free.FreeImageDto;

@Mapper
public interface FreeMapper {

    // 게시글 CRUD
    @Insert("""
        INSERT INTO free (user_id, title, content, view_count, like_count, created_at, updated_at)
        VALUES (#{userId}, #{title}, #{content}, 0, 0, NOW(), NOW())
    """)
    @Options(useGeneratedKeys = true, keyProperty = "freeId")
    void insertFree(FreeDto dto);

    @Select("SELECT * FROM free WHERE free_id = #{freeId}")
    FreeDto getFreeById(Long freeId);
    
    /** 조회수 1 증가 */
    @Update("UPDATE free SET view_count = view_count + 1 WHERE free_id = #{freeId}")
    void incrementViewCount(Long freeId);
    
    //추천 비추천 
    @Update("UPDATE free SET like_count    = like_count    + #{delta} WHERE free_id = #{freeId}")
    void updateLikeCount(@Param("freeId") Long freeId, @Param("delta") int delta);
    
    @Update("UPDATE free SET dislike_count = dislike_count + #{delta} WHERE free_id = #{freeId}")
    void updateDislikeCount(@Param("freeId") Long freeId, @Param("delta") int delta);

    @Select("""
    	      SELECT f.*, u.nickname
    	      FROM free f
    	      JOIN user u ON f.user_id = u.user_id
    	      ORDER BY f.created_at DESC
    	    """)
    List<FreeDto> getAllFree();

    @Select("SELECT image_url FROM free_image WHERE free_id = #{freeId}")
    List<String> getImageUrlsByFreeId(Long freeId);

    @Update("""
        UPDATE free SET title = #{title}, content = #{content}, updated_at = NOW()
        WHERE free_id = #{freeId}
    """)
    void updateFree(FreeDto dto);

    @Delete("DELETE FROM free WHERE free_id = #{freeId}")
    void deleteFree(Long freeId);

    // 댓글 CRUD
    @Insert("""
        INSERT INTO free_comments (free_id, user_id, content, created_at, is_deleted)
        VALUES (#{freeId}, #{userId}, #{content}, NOW(), false)
    """)
    @Options(useGeneratedKeys = true, keyProperty = "commentId")
    void insertComment(FreeCommentDto dto);

    @Select("SELECT * FROM free_comments WHERE free_id = #{freeId} AND is_deleted = false ORDER BY created_at ASC")
    List<FreeCommentDto> getCommentsByFreeId(Long freeId);

    @Select("SELECT * FROM free_comments WHERE comment_id = #{commentId} AND free_id = #{freeId}")
    FreeCommentDto getComment(@Param("commentId") Long commentId, @Param("freeId") Long freeId);

    @Update("""
        UPDATE free_comments SET content = #{content} WHERE comment_id = #{commentId} AND free_id = #{freeId}
    """)
    void updateComment(FreeCommentDto dto);

    @Delete("""
        DELETE FROM free_comments WHERE comment_id = #{commentId} AND free_id = #{freeId}
    """)
    void deleteComment(@Param("commentId") Long commentId, @Param("freeId") Long freeId);

    // 이미지 추가
    @Insert("""
        INSERT INTO free_image (free_id, image_url)
        VALUES (#{freeId}, #{imageUrl})
    """)
    void insertImage(FreeImageDto dto);
}