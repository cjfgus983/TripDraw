package HyeonRi.TripDrawApp.dto.board.tripBoard;

import java.util.List;
import java.util.Arrays;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripBoardWithRouteDto {
    private Long   planBoardId;
    private String boardTitle;
    private String boardContent;

    // 원본 콤마결합 문자열
    private String boardCategoryConcat;
    // 변환된 리스트
    @Setter(AccessLevel.NONE)
    private List<String> boardCategoryList;
    
    private String nickname;
    private String region;

    private String planCode;
    private String createdAt;
    private boolean favorite;

    private String routeConcat;
    @Setter(AccessLevel.NONE)
    private List<String> route;

    // boardCategoryConcat → boardCategoryList 변환
    public void setBoardCategoryConcat(String boardCategoryConcat) {
        this.boardCategoryConcat = boardCategoryConcat;
        if (boardCategoryConcat != null && !boardCategoryConcat.isEmpty()) {
            this.boardCategoryList = Arrays.asList(boardCategoryConcat.split(","));
        }
    }

    // routeConcat → route 변환
    public void setRouteConcat(String routeConcat) {
        this.routeConcat = routeConcat;
        if (routeConcat != null && !routeConcat.isEmpty()) {
            this.route = Arrays.asList(routeConcat.split(","));
        }
    }
}