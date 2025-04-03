package tqs.homework.canteen.EnumTypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MealType {
    SOUP("soup"),
    MEAT("meat"),
    FISH("FISH");

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
            case "meat":
                return MealType.MEAT;
            case "fish":
                return MealType.FISH;
        }
        throw new IllegalArgumentException("\"" + name +"\" is not a valid meal type");
    }
}
