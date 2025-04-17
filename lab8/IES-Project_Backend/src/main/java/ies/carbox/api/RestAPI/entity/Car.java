package ies.carbox.api.RestAPI.entity;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import ies.carbox.api.RestAPI.validation.YearRange;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a car entity in the system.
 *
 * <p>This entity contains all the information related to a car, including its
 * brand, model, year, license plate, and the owner of the car.</p>
 *
 * <p>Car data attributes:</p>
 * <ul>
 *     <li><b>id</b>: Unique identifier for the car.</li>
 *     <li><b>brand</b>: Brand of the car (e.g., Toyota, Ford).</li>
 *     <li><b>model</b>: Model of the car (e.g., Corolla, Focus).</li>
 *     <li><b>year</b>: Year of manufacture, validated by {@link YearRange}.</li>
 *     <li><b>licensePlate</b>: License plate number of the car.</li>
 *     <li><b>owner</b>: The user who owns the car, represented by the {@link User} entity.</li>
 * </ul>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Document(collection = "Cars")
public class Car {

    /** Unique identifier for the car. */
    @Id
    @Field("ecu_id")
    private String ecuId;

    /** Brand of the car (e.g., Toyota, Ford). */
    @Field("brand")
    @NotBlank(message = "Brand is required")
    private String brand;

    /** Model of the car (e.g., Corolla, Focus). */
    @Field("model")
    @NotBlank(message = "Model is required")
    private String model;

    /** Year of manufacture, validated by {@link YearRange}. */
    @Field("year")
    @YearRange
    private Integer year;

    /** License plate number of the car. */
    @Field("license_plate")
    @NotBlank(message = "License plate is required")
    private String licensePlate;

    /** Date of the last revision */
    @Field("last_revision")
    private String lastRevision;

    /** Model of the tires */
    @Field("tires")
    private String tires;

    /** Model of the motor */
    @Field("motor")
    private String motor;

    /** Model and capacity of the fuel tank */
    @Field("tank")
    private String tank;

    /** Max speed of the vehicle */
    @Field("max_speed")
    private Float maxSpeed;

    /** Top horsepower of the vehicle */
    @Field("horsepower")
    private Integer horsepower;

    /** Remaining predicted fuel autonomy */
    @Field("autonomy")
    private Float autonomy;

    private String location;
    private boolean carStatus;

    /**
     * Returns a string representation of the car.
     *
     * @return A string containing the car's id, brand, model, year, license plate, and owner.
     */
    @Override
    public String toString() {
        return "Car [ecuId=" + ecuId + ", brand=" + brand + ", model=" + model + ", year=" + year +
               ", licensePlate=" + licensePlate + "]";
    }
}
