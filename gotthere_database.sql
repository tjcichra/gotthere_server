CREATE DATABASE gotthere_database;

USE gotthere_database;

CREATE TABLE locations (
	id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
	insertion_datetime timestamp NOT NULL DEFAULT current_timestamp(),
	bearing DECIMAL(5,2),
	latitude DECIMAL(5,2),
	longitude DECIMAL(5,2),
	speed DECIMAL(5,2)
);

CREATE USER 'remote'@'%' IDENTIFIED BY 'test';
GRANT ALL ON gotthere_database TO 'remote'@'%';