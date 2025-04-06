package tqs.homework.canteen.DTOs;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Forecast {
    private LocalDate date;
    
    @JsonProperty("maxtemp")
    private double maxTemp;
    
    @JsonProperty("mintemp")
    private double minTemp;
    
    @JsonProperty("avghumidity")
    private int avgHumidity;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("icon")
    private String icon;
}
