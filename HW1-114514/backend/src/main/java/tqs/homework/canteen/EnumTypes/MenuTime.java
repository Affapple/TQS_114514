package tqs.homework.canteen.EnumTypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MenuTime {
    LUNCH("LUNCH"),
    DINNER("DINNER");

    private String name;
    MenuTime(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public static MenuTime fromName(String name) {
        for (MenuTime time : MenuTime.values()) {
            if (time.name.equals(name)) {
                return time;
            }
        }
        throw new IllegalArgumentException("\"" + name +"\" is not a valid menu time");
    }
}
