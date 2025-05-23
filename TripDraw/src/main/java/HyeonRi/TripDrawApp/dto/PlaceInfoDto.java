// src/main/java/HyeonRi/TripDrawApp/dto/PlaceInfo.java
package HyeonRi.TripDrawApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceInfoDto {
    private String name;
    private String description;
    private String photoURL;
}
