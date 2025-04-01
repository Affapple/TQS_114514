package tqs.homework.canteen.EnumTypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MealType {
    SOUP("soup"),
    PLATE("plate"),
    PLATE_OPT("plate_opt"),
    PLATE_VEG("plate_veg");

    private String name;
    MealType(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public static MealType fromName(String name) {
        switch (name) {
            case "soup":
                return MealType.SOUP;
            case "plate":
                return MealType.PLATE;
            case "plate_opt":
                return MealType.PLATE_OPT;
            case "plate_veg":
                return MealType.PLATE_VEG;
        }
        throw new IllegalArgumentException("\"" + name +"\" is not a valid meal type");
    }
}
