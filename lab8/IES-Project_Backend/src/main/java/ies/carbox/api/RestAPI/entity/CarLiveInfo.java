package ies.carbox.api.RestAPI.entity;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Represents live information about a car during a trip.
 *
 * <p>The unique identifier for this entity is represented by the embedded ID {@link CarLiveInfoId}.
 * This entity includes various attributes related to the car's status and performance.</p>
 *
 * <p>Attributes:</p>
 * <ul>
 * <li>Car Status: Indicates if the car is operational.</li>
 * <li>Oil Level: The current oil level in the car.</li>
 * <li>Battery Charge: The battery charge percentage.</li>
 * <li>Speed: The current speed of the car.</li>
 * <li>RPM: The current revolutions per minute of the engine.</li>
 * <li>Gas Level: The current gas level in the tank.</li>
 * <li>Location: The current geographical location of the car.</li>
 * <li>Motor Temperature: The current temperature of the car's engine.</li>
 * <li>ABS: Indicates whether the anti-lock braking system is active.</li>
 * <li>Torque: The current torque output of the engine.</li>
 * <li>Tire Pressure: The current pressure of the tires.</li>
 * <li>Errors: A list of error messages related to the car's performance.</li>
 * </ul>
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "CarLiveInfo")
public class CarLiveInfo {

    /** ID of the car associated with live info */
    @Field("car_id")
    String carId;

    /** Id of the trip whose this live info belongs */
    @Field("trip_id")
    String tripId;

    /** Timestamp of the gathered data */
    @Field("timestamp")
    LocalDateTime timestamp;

    /** Current status of the vehicle (true: on; false: off) */
    @Field("car_status")
    private boolean carStatus;

    /** Current oil leve */
    @Field("oil_level")
    private float oilLevel;

    /** Current battery charge */
    @Field("battery_charge")
    private float batteryCharge;

    /** Current car speed */
    @Field("speed")
    private float speed;

    /** Current car motor rpm */
    @Field("rpm")
    private int rpm;

    /** Current car gas level (in liters) */
    @Field("gas_level")
    private float gasLevel;

    /** Current car location */
    @Field("location")
    private String location;

    /** Current motor temperature */
    @Field("motor_temperature")
    private float motorTemperature;

    /** Current status of the abs (true: activated; false: deactivated) */
    @Field("abs")
    private boolean abs;

    /** Current torque produced by the motor */
    @Field("torque")
    private float torque;

    /** Current tire pressures */
    @Field("tire_pressure")
    private List<Float> tirePressure;

    /** List of car users */
    @Field("errors") // Column name in the collection table
    private List<String> errors;
}
