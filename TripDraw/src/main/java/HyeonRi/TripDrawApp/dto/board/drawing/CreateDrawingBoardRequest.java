package HyeonRi.TripDrawApp.dto.board.drawing;

import lombok.Data;

@Data
public class CreateDrawingBoardRequest {
    private Long drawingId;
    private String title;
    private String content;   // null 가능
}
