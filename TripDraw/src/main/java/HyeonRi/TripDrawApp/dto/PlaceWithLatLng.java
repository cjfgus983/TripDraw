package HyeonRi.TripDrawApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceWithLatLng {
    private String name;
    private String description;
    private String photoURL;
    private double latitude;
    private double longitude;
}
