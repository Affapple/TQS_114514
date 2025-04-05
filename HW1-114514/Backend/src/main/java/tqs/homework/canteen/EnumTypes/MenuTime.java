package tqs.homework.canteen.EnumTypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MenuTime {
    LUNCH("lunch"),
    DINNER("dinner");

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
        switch (name) {
            case "lunch":
                return MenuTime.LUNCH;

            case "dinner":
                return MenuTime.DINNER;
        }
        throw new IllegalArgumentException("\"" + name +"\" is not a valid meal time");
    }
}
