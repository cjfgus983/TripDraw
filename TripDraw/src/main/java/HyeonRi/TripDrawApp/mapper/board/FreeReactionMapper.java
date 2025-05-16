package HyeonRi.TripDrawApp.mapper.board;

import org.apache.ibatis.annotations.*;

@Mapper
public interface FreeReactionMapper {

    // 1) 사용자가 이 게시글에 어떤 반응을 했는지 조회
    @Select("""
      SELECT reaction_type 
        FROM free_reaction
       WHERE free_id = #{freeId} 
         AND user_id = #{userId}
    """)
    String getUserReaction(@Param("freeId") Long freeId, @Param("userId") Long userId);

    // 2) 새 반응 추가
    @Insert("""
      INSERT INTO free_reaction (free_id, user_id, reaction_type)
      VALUES (#{freeId}, #{userId}, #{type})
    """)
    void insertReaction(@Param("freeId") Long freeId,
                        @Param("userId") Long userId,
                        @Param("type") String type);

    // 3) 기존 반응 삭제
    @Delete("""
      DELETE FROM free_reaction
       WHERE free_id = #{freeId}
         AND user_id = #{userId}
    """)
    void deleteReaction(@Param("freeId") Long freeId, @Param("userId") Long userId);
}
