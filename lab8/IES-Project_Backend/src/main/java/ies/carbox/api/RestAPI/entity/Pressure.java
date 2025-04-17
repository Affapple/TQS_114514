package ies.carbox.api.RestAPI.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.*;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
/**
 * Pressure
 *  Represents the tire pressure's of the car (in psi)
 */
public class Pressure {
    /** Front Left tire pressure (in psi) */
    @Field("front_left")
    private float frontLeft;

    /** Front Right tire pressure (in psi) */
    @Field("front_right")
    private float frontRight;

    /** Back Left tire pressure (in psi) */
    @Field("back_left")
    private float backLeft;

    /** Back Right tire pressure (in psi) */
    @Field("back_right")
    private float backRight;
}

