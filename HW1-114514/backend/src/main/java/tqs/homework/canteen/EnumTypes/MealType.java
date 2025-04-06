package tqs.homework.canteen.EnumTypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MealType {
    SOUP("SOUP"),
    MEAT("MEAT"),
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
        for (MealType type : MealType.values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("\"" + name +"\" is not a valid meal type");
    }
}
