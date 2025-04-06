package tqs.homework.canteen.EnumTypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ReservationStatus {
    ACTIVE("ACTIVE"),
    CANCELLED("CANCELLED"),
    USED("USED");

    private String name;
    ReservationStatus(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public static ReservationStatus fromName(String name) {
        for (ReservationStatus status : ReservationStatus.values()) {
            if (status.name.equals(name)) {
                return status;
            }
        }
        throw new IllegalArgumentException("\"" + name +"\" is not a valid reservation status");
    }
}

