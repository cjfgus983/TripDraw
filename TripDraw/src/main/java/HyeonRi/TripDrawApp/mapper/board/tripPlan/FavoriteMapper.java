package HyeonRi.TripDrawApp.mapper.board.tripPlan;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FavoriteMapper {
	  @Insert("INSERT INTO user_favorite_boards(user_id, plan_board_id) VALUES(#{userId}, #{boardId})")
	  void addFavorite(@Param("userId") Long userId, @Param("boardId") Long boardId);

	  @Delete("DELETE FROM user_favorite_boards WHERE user_id=#{userId} AND plan_board_id=#{boardId}")
	  void removeFavorite(@Param("userId") Long userId, @Param("boardId") Long boardId);

	  @Select("SELECT COUNT(*) FROM user_favorite_boards WHERE user_id=#{userId} AND plan_board_id=#{boardId}")
	  boolean isFavorited(@Param("userId") Long userId, @Param("boardId") Long boardId);

	  @Select("SELECT plan_board_id FROM user_favorite_boards WHERE user_id=#{userId}")
	  List<Long> findFavoritesByUser(@Param("userId") Long userId);
	}
