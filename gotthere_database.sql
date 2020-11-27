CREATE DATABASE gotthere_database;

USE gotthere_database;

--Change existing database to jpa database
INSERT INTO location (bearing, date_time, insertion_date_time, latitude, longitude, speed)
SELECT bearing, real_datetime, insertion_datetime, latitude, longitude, speed
FROM locations;

CREATE TABLE `user` (
	id int(11) PRIMARY KEY AUTO_INCREMENT,
	username VARCHAR(255) NOT NULL,
	password VARCHAR(255) NOT NULL
);

--Change existing database to jpa database
INSERT INTO `user` (username, password)
SELECT username, password
FROM users;

CREATE USER 'remote'@'%' IDENTIFIED BY 'test';
GRANT ALL ON gotthere_database.locations TO 'remote'@'%';