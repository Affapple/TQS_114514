package ies.carbox.api.RestAPI.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.*;

/**
 * TripInfo
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonSerialize
@Document(collection = "TripInfos")
public class TripInfo {
    /** Car Id */
    @Field("car_id")
    String carId;

    /** Trip Id */
    @Field("trip_id")
    String tripId;

    /** Trip Start Date */
    @Field("trip_start")
    LocalDateTime tripStart;

    @Field("trip_end")
    /** Trip End Date */
    LocalDateTime tripEnd;

    @Field("trip_speeds")
    /** Trip Speeds */
    List<Float> tripSpeeds;

    @Field("trip_rpm")
    List<Integer> tripRpm;

    @Field("trip_motor_temp")
    List<Float> tripMotorTemp;

    @Field("trip_torque")
    List<Float> tripTorque;


    @Override
    public String toString() {
        return "TripInfo [carId=" + carId + ", tripId=" + tripId + ", Trip_start=" + tripStart + ", Trip_end=" + tripEnd + "]";
    }
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

}
