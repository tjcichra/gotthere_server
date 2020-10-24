CREATE DATABASE gotthere_database2;

USE gotthere_database2;

CREATE TABLE locations2 (
	id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
	insertion_datetime timestamp NOT NULL DEFAULT current_timestamp(),
	bearing DECIMAL(5,2),
	latitude DECIMAL(5,2),
	longitude DECIMAL(5,2),
	speed DECIMAL(5,2)
);