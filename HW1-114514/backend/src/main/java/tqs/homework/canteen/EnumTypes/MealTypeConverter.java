package tqs.homework.canteen.EnumTypes;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MealTypeConverter implements AttributeConverter<MealType, String> {
    @Override
    public String convertToDatabaseColumn(MealType attribute) {
        return attribute == null ? null : attribute.getName();
    }

    @Override
    public MealType convertToEntityAttribute(String dbData) {
        return MealType.fromName(dbData);
    }
}
