package tqs.homework.canteen.EnumTypes;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MenuTimeConverter implements AttributeConverter<MenuTime, String> {
    @Override
    public String convertToDatabaseColumn(MenuTime attribute) {
        return attribute == null ? null : attribute.getName();
    }

    @Override
    public MenuTime convertToEntityAttribute(String dbData) {
        return MenuTime.fromName(dbData);
    }
}
