package tqs.homework.canteen.EnumTypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MealTime {
    LUNCH("lunch"),
    DINNER("dinner");

    private String name;
    MealTime(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public static MealTime fromName(String name) {
        switch (name) {
            case "lunch":
                return MealTime.LUNCH;

            case "dinner":
                return MealTime.DINNER;
        }
        throw new IllegalArgumentException("\"" + name +"\" is not a valid meal time");
    }
}
