package com.tim.gotthere_server.database;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;

@Entity
@Getter @Setter @NoArgsConstructor
public class Bookmark {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private double latitude;

	private double longitude;

	private String name;

	@JsonFormat(timezone = "America/New_York")
	private Date dateCreated;

	public Bookmark(double latitude, double longitude, String name, Date dateCreated) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
		this.dateCreated = dateCreated;
	}
}
