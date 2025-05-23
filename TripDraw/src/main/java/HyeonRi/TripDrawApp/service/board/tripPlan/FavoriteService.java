package HyeonRi.TripDrawApp.service.board.tripPlan;

import java.util.List;

import org.springframework.stereotype.Service;

import HyeonRi.TripDrawApp.mapper.board.tripPlan.FavoriteMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {
  private final FavoriteMapper favoriteMapper;

  public void addFavorite(Long userId, Long boardId) {
    if (!favoriteMapper.isFavorited(userId, boardId)) {
      favoriteMapper.addFavorite(userId, boardId);
    }
  }

  public void removeFavorite(Long userId, Long boardId) {
    favoriteMapper.removeFavorite(userId, boardId);
  }

  public boolean isFavorited(Long userId, Long boardId) {
    return favoriteMapper.isFavorited(userId, boardId);
  }

  public List<Long> getUserFavorites(Long userId) {
    return favoriteMapper.findFavoritesByUser(userId);
  }
}
