package tqs.homework.canteen.EnumTypes;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ReservationStatusConverter implements AttributeConverter<ReservationStatus, String> {
    @Override
    public String convertToDatabaseColumn(ReservationStatus attribute) {
        return attribute == null ? null : attribute.getName();
    }

    @Override
    public ReservationStatus convertToEntityAttribute(String dbData) {
        return ReservationStatus.fromName(dbData);
    }
}
