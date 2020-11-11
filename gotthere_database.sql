CREATE DATABASE gotthere_database;

USE gotthere_database;

CREATE TABLE locations (
	id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
	real_datetime timestamp NULL,
	insertion_datetime timestamp NOT NULL DEFAULT current_timestamp(),
	bearing DECIMAL(5,2),
	latitude DECIMAL(10,6),
	longitude DECIMAL(10,6),
	speed DECIMAL(5,2)
);

CREATE TABLE users (
	id int PRIMARY KEY AUTO_INCREMENT,
	username VARCHAR(100) NOT NULL,
	password VARCHAR(100) NOT NULL
);

CREATE USER 'remote'@'%' IDENTIFIED BY 'test';
GRANT ALL ON gotthere_database.locations TO 'remote'@'%';