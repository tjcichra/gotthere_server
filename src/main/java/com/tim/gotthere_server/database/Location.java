package com.tim.gotthere_server.database;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter @Setter @NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @JsonFormat(timezone = "America/New_York")
    private Date dateTime;

    @JsonFormat(timezone = "America/New_York")
    private Date insertionDateTime;

    private double latitude;

    private double longitude;

    private double speed;

    private double bearing;

    public Location(Date dateTime, Date insertionDateTime, double latitude, double longitude, double speed, double bearing) {
        this.dateTime = dateTime;
        this.insertionDateTime = insertionDateTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.bearing = bearing;
    }
}


