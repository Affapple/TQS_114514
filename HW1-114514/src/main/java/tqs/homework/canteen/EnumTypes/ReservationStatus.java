package tqs.homework.canteen.EnumTypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ReservationStatus {
    ACTIVE("active"),
    USED("used"),
    CANCELLED("cancelled");

    private final String name;
    ReservationStatus(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return this.name;
    }

    @JsonCreator
    public static ReservationStatus fromName(String name) {
        switch (name) {
            case "active":
                return ReservationStatus.ACTIVE;
            case "used":
                return ReservationStatus.USED;
            case "cancelled":
                return ReservationStatus.CANCELLED;
        }
        throw new IllegalArgumentException("\"" + name +"\" is not a valid status");
    }
}

